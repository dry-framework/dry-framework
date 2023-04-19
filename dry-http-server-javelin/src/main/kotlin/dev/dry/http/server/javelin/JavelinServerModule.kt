package dev.dry.http.server.javelin

import dev.dry.application.launcher.ApplicationLaunchContext
import dev.dry.application.module.ApplicationModule
import dev.dry.application.module.ApplicationModuleProvider
import dev.dry.application.module.ConfigurableModule
import dev.dry.application.module.LaunchableModule
import dev.dry.common.exception.Exceptions
import dev.dry.dependency.DependencyRegistry
import dev.dry.dependency.DependencyResolver
import dev.dry.http.server.RequestAttributes
import dev.dry.http.server.RequestAttributes.DefaultRequestAttributes
import dev.dry.http.server.ResponseFactory
import dev.dry.http.server.configuration.ServerConfiguration
import dev.dry.http.server.error.HttpErrors
import dev.dry.http.server.handler.binding.RequestHandlerBinding
import dev.dry.http.server.handler.binding.RequestHandlerOperationBinding
import dev.dry.http.server.javelin.function.MapHttpMethod
import dev.dry.http.server.javelin.function.MapHttpStatus
import dev.dry.http.server.parameter.AuthenticatedPrincipalFactory
import io.javalin.Javalin
import io.javalin.http.Handler
import io.javalin.security.AccessManager
import org.slf4j.LoggerFactory

class JavelinServerModule : ApplicationModuleProvider, ConfigurableModule, LaunchableModule {
    override fun provide(): ApplicationModule = JavelinServerModule()

    override fun configure(ctx: ApplicationLaunchContext, registry: DependencyRegistry) {
        registry.instance<RequestAttributes>(requestAttributes)
        registry.singleton { ResponseFactory(resolve(), resolve(), resolve()) }
    }

    override fun launch(ctx: ApplicationLaunchContext, resolver: DependencyResolver) {
        logger.info("launching Javelin Server")
        val config: ServerConfiguration = resolver.resolveOrNull() ?: ServerConfiguration.default()

        val responseFactory: ResponseFactory = resolver.resolve()
        val authenticatedPrincipalFactory = resolver.resolve<AuthenticatedPrincipalFactory>()
        val accessManager = configureAccessManager(authenticatedPrincipalFactory, requestAttributes, responseFactory)
        val app = configureJavelin(config, accessManager)

        configureErrorHandling(app, responseFactory)

        configureHandlers(resolver) { operations ->
            operations.forEach { operation ->
                configureHandler(app, resolver, operation)
            }
        }

        app.start(config.port)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(JavelinServerModule::class.java)

        private val requestAttributes = DefaultRequestAttributes()

        fun configureHandlers(resolver: DependencyResolver, block: (List<RequestHandlerOperationBinding>) -> Unit) {
            logger.info("resolving HTTP handlers")
            val bindings = resolver.resolveAll<RequestHandlerBinding>()
            logger.info("resolved ${bindings.size} request handler bindings")

            val operations = mutableListOf<RequestHandlerOperationBinding>()
            for (binding in bindings) {
                operations.addAll(binding.operations)
            }
            logger.info("resolved ${operations.size} request handler operation bindings")

            block(operations)
        }

        private fun configureHandler(
            app: Javalin,
            resolver: DependencyResolver,
            operation: RequestHandlerOperationBinding,
        ) {
            val method = MapHttpMethod(operation.method)
            val path = operation.path.toString()
            val requestHandler = operation.resolveRequestHandler(resolver)
            val handler = Handler { ctx ->
                val request = JavelinRequestAdaptor(ctx)
                val response = requestHandler(request)
                val status = MapHttpStatus(response.status)
                ctx.status(status).result(response.bodyBuffer().array())
            }
            val requiredRoles = operation.requiredRoles.map(::JavelinRouteRole).toTypedArray()

            app.addHandler(method, path, handler, *requiredRoles)
        }

        private fun configureJavelin(
            config: ServerConfiguration,
            accessManager: AccessManager,
        ): Javalin {
            return Javalin.create { c ->
                c.showJavalinBanner = false
                c.routing.contextPath = config.contextPath
                c.accessManager(accessManager)
            }
        }

        private fun configureAccessManager(
            authenticatedPrincipalFactory: AuthenticatedPrincipalFactory,
            requestAttributes: RequestAttributes,
            responseFactory: ResponseFactory,
        ): JavelinAccessManager {
            return JavelinAccessManager(
                authenticatedPrincipalFactory = authenticatedPrincipalFactory,
                requestAttributes = requestAttributes,
                responseFactory = responseFactory,
            )
        }

        private fun configureErrorHandling(app: Javalin, responseFactory: ResponseFactory) {
            app.exception(Exception::class.java) { ex, ctx ->
                logger.error("unhandled exception - {}", Exceptions.getMessageChain(ex))
                ctx.status(500)
            }
            app.error(500) { ctx ->
                val response = responseFactory.constructResponse(ctx.requestAdaptor(), HttpErrors.SERVER_ERROR)
                ctx.result(response.bodyStream())
            }
            app.error(404) { ctx ->
                val response = responseFactory.constructResponse(ctx.requestAdaptor(), HttpErrors.NOT_FOUND)
                ctx.result(response.bodyStream())
            }
        }
    }
}
