package dev.dry.application.launcher

import dev.dry.application.logging.LoggingThreadContextListener
import dev.dry.application.module.ApplicationModule
import dev.dry.application.module.ApplicationModuleLoader
import dev.dry.application.module.ConfigurableModule
import dev.dry.application.module.LaunchableModule
import dev.dry.common.concurrent.ThreadContext
import dev.dry.common.exception.Exceptions
import dev.dry.common.function.Either
import dev.dry.common.function.onFailure
import dev.dry.common.function.onSuccess
import dev.dry.common.time.StopWatch
import dev.dry.configuration.properties.ConfigurationProperties
import dev.dry.configuration.resource.ConfigurationResourceLoader
import dev.dry.dependency.DependencyRegistry
import dev.dry.dependency.DependencyResolver
import org.slf4j.LoggerFactory

open class DefaultApplicationLauncher : ApplicationLauncher() {
    companion object {
        fun moduleName(module: ApplicationModule): String {
            return module::class.java.simpleName
        }
    }

    private val logger = LoggerFactory.getLogger(DefaultApplicationLauncher::class.java)

    override fun launch(
        launchConfiguration: ApplicationLaunchConfiguration,
    ): Either<Throwable, DependencyResolver> {
        ThreadContext.addListener(LoggingThreadContextListener)
        val stopWatch = StopWatch.start()
        logger.info("launching application")
        return super.launch(launchConfiguration).apply {
            val elapsedTimeMillis = stopWatch.elapsedTimeMillis
            onFailure {
                logger.error("failed to launch application in {}ms: {}",
                    elapsedTimeMillis,
                    Exceptions.getMessageChain(it)
                )
            }
            onSuccess {
                logger.info("launched application in {}ms", elapsedTimeMillis)
            }
        }
    }

    override fun loadConfigurationProperties(
        applicationName: String,
        environmentVariableNamePrefix: String,
        configurationResourceLoaders: List<ConfigurationResourceLoader>,
    ): ConfigurationProperties {
        logger.info(
            "loading configuration properties for application '{}' with environment variable name prefix '{}' and " +
                    "{} configuration resource loaders {}",
            applicationName,
            environmentVariableNamePrefix,
            configurationResourceLoaders.size,
            configurationResourceLoaders.map { it::class.qualifiedName },
        )
        return super.loadConfigurationProperties(
            applicationName,
            environmentVariableNamePrefix,
            configurationResourceLoaders
        )
    }

    override fun loadApplicationLaunchContext(
        configurationProperties: ConfigurationProperties
    ): ApplicationLaunchContext {
        logger.info("loading application launch context")
        return ApplicationLaunchContext(configurationProperties)
    }

    override fun loadApplicationModules(
        applicationModuleLoader: ApplicationModuleLoader
    ): Collection<ApplicationModule> {
        logger.info("loading application modules")
        val modules = super.loadApplicationModules(applicationModuleLoader)
        logger.info("loaded ${modules.size} application modules")
        modules.forEach { logger.info(" - '{}'", moduleName(it)) }
        return modules
    }

    override fun configureModules(
        modules: Collection<ConfigurableModule>,
        ctx: ApplicationLaunchContext,
        dependencyRegistry: DependencyRegistry,
    ) {
        logger.info("configuring application modules")
        modules.forEach { logger.info(" - '{}'", moduleName(it)) }
        super.configureModules(modules, ctx, dependencyRegistry)
    }

    override fun configureModule(
        module: ConfigurableModule,
        ctx: ApplicationLaunchContext,
        dependencyRegistry: DependencyRegistry,
    ) {
        logger.info("configuring '{}'", moduleName(module))
        super.configureModule(module, ctx, dependencyRegistry)
    }

    override fun launchModules(
        modules: Collection<LaunchableModule>,
        ctx: ApplicationLaunchContext,
        dependencyResolver: DependencyResolver
    ) {
        logger.info("launching ${modules.size} modules")
        modules.forEach { logger.info(" - '{}'", moduleName(it)) }
        super.launchModules(modules, ctx, dependencyResolver)
    }

    override fun launchModule(
        launchableModule: LaunchableModule,
        ctx: ApplicationLaunchContext,
        dependencyResolver: DependencyResolver
    ) {
        logger.info("launching '{}'", moduleName(launchableModule))
        super.launchModule(launchableModule, ctx, dependencyResolver)
    }
}
