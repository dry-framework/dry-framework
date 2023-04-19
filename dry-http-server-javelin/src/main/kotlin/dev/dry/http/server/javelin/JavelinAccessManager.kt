package dev.dry.http.server.javelin

import dev.dry.common.concurrent.ThreadContext
import dev.dry.common.error.CodedError
import dev.dry.http.server.RequestAttributes
import dev.dry.http.server.ResponseFactory
import dev.dry.http.server.error.HttpErrors
import dev.dry.http.server.parameter.AuthenticatedPrincipalFactory
import dev.dry.security.auth.Principal
import io.javalin.http.Context
import io.javalin.http.Handler
import io.javalin.security.AccessManager
import io.javalin.security.RouteRole
import org.slf4j.LoggerFactory

class JavelinAccessManager(
    private val authenticatedPrincipalFactory: AuthenticatedPrincipalFactory,
    private val requestAttributes: RequestAttributes,
    private val responseFactory: ResponseFactory,
) : AccessManager {
    private val logger = LoggerFactory.getLogger(JavelinAccessManager::class.java)

    override fun manage(handler: Handler, ctx: Context, routeRoles: Set<RouteRole>) {
        val threadContext = ThreadContext.initialise()
        val request = ctx.requestAdaptor()
        authenticatedPrincipalFactory.construct(request).fold(
            { error -> onPrincipalConstructionError(ctx, error) },
            { principal -> onPrincipalConstructed(handler, ctx, routeRoles, principal, threadContext) },
        )
    }

    private fun onPrincipalConstructionError(ctx: Context, error: CodedError) {
        logger.info("failed to construct principal - ${error.exceptionMessage}")
        val request = ctx.requestAdaptor()
        val response = responseFactory.constructResponse(request, error)
        ctx.response(response)
    }

    private fun onPrincipalConstructed(
        handler: Handler,
        ctx: Context,
        routeRoles: Set<RouteRole>,
        principal: Principal?,
        threadContext: ThreadContext,
    ) {
        val request = ctx.requestAdaptor()
        val roles: List<JavelinRouteRole> = if (principal != null) {
            requestAttributes.principal(request, principal)
            threadContext.principal(principal)
            principal.roles.map(::JavelinRouteRole)
        } else emptyList()

        if (routeRoles.isEmpty() || roles.intersect(routeRoles).isNotEmpty()) {
            handler.handle(ctx)
        } else {
            val response = responseFactory.constructResponse(request, HttpErrors.UNAUTHORIZED_ERROR)
            ctx.response(response)
        }
    }
}
