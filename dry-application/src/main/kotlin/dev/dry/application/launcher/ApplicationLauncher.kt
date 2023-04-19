package dev.dry.application.launcher

import dev.dry.application.module.ApplicationModule
import dev.dry.application.module.ApplicationModuleLoader
import dev.dry.application.module.ConfigurableModule
import dev.dry.application.module.ConfigurationResourceLoaderModule
import dev.dry.application.module.LaunchableModule
import dev.dry.configuration.properties.ConfigurationProperties
import dev.dry.common.function.Either
import dev.dry.configuration.format.properties.PropertiesConfigurationFormat
import dev.dry.configuration.properties.ConfigurationPropertiesBuilder
import dev.dry.configuration.properties.system.SystemConfigurationProperties
import dev.dry.configuration.resource.ConfigurationResourceLoader
import dev.dry.configuration.resource.DefaultConfigurationResource
import dev.dry.configuration.resource.DefaultConfigurationResourceLoader
import dev.dry.dependency.DependencyRegistry
import dev.dry.dependency.DependencyResolver

abstract class ApplicationLauncher {
    protected open fun getConfigurationResourceLoaders(
        configurationResourceLoaderModules: List<ConfigurationResourceLoaderModule>
    ): List<ConfigurationResourceLoader> = configurationResourceLoaderModules.map(::getConfigurationResourceLoader)

    protected open fun getConfigurationResourceLoader(
        configurationResourceLoaderModule: ConfigurationResourceLoaderModule
    ): ConfigurationResourceLoader = configurationResourceLoaderModule.configurationResourceLoader()

    protected open fun loadConfigurationProperties(
        applicationName: String,
        environmentVariableNamePrefix: String,
        configurationResourceLoaders: List<ConfigurationResourceLoader>,
    ): ConfigurationProperties {
        val systemConfigurationProperties = SystemConfigurationProperties.load(environmentVariableNamePrefix)
        val activeProfiles = systemConfigurationProperties.activeProfiles
        //val externalConfigLocation = systemConfigurationProperties.externalConfigLocation

        val builder = ConfigurationPropertiesBuilder(
            systemConfigurationProperties,
            configurationResourceLoaders +
                    DefaultConfigurationResourceLoader(PropertiesConfigurationFormat) {
                        DefaultConfigurationResource.classpath("/$it")
                    }
        )
            .resourceName(applicationName)
            .resourceName("application")

        return builder.build()
    }

    protected abstract fun loadApplicationLaunchContext(
        configurationProperties: ConfigurationProperties
    ): ApplicationLaunchContext

    protected open fun loadApplicationModules(
        applicationModuleLoader: ApplicationModuleLoader
    ): Collection<ApplicationModule> = applicationModuleLoader.load()

    protected open fun configureModules(
        modules: Collection<ConfigurableModule>,
        ctx: ApplicationLaunchContext,
        dependencyRegistry: DependencyRegistry,
    ) {
        modules.forEach { configureModule(it, ctx, dependencyRegistry) }
    }

    protected open fun configureModule(
        module: ConfigurableModule,
        ctx: ApplicationLaunchContext,
        dependencyRegistry: DependencyRegistry,
    ) {
        module.configure(ctx, dependencyRegistry)
    }

    protected open fun launchModules(
        modules: Collection<LaunchableModule>,
        ctx: ApplicationLaunchContext,
        dependencyResolver: DependencyResolver,
    ) {
        modules.forEach { launchModule(it, ctx, dependencyResolver) }
    }

    protected open fun launchModule(
        launchableModule: LaunchableModule,
        ctx: ApplicationLaunchContext,
        dependencyResolver: DependencyResolver,
    ) {
        launchableModule.launch(ctx, dependencyResolver)
    }

    open fun launch(
        applicationName: String = "app",
        environmentVariableNamePrefix: String = "APP"
    ): Either<Throwable, DependencyResolver> =
        launch(
            ApplicationLaunchConfiguration(
            applicationName = applicationName,
            environmentVariableNamePrefix = environmentVariableNamePrefix
        )
        )

    open fun launch(
        launchConfiguration: ApplicationLaunchConfiguration
    ): Either<Throwable, DependencyResolver> {
        return try {
            val modules = loadApplicationModules(launchConfiguration.applicationModuleLoader)

            val configurationResourceLoaderModules = modules.filterIsInstance<ConfigurationResourceLoaderModule>()
            val configurationResourceLoaders = getConfigurationResourceLoaders(configurationResourceLoaderModules)
            val configurationProperties = loadConfigurationProperties(
                launchConfiguration.applicationName,
                launchConfiguration.environmentVariableNamePrefix,
                configurationResourceLoaders,
            )

            val ctx = loadApplicationLaunchContext(configurationProperties)

            val resolverBuilder = launchConfiguration.dependencyRegistry
            val configurableModules = modules.filterIsInstance<ConfigurableModule>()
            configureModules(configurableModules, ctx, resolverBuilder)

            val resolver = resolverBuilder.toResolver()
            val launchableModules = modules.filterIsInstance<LaunchableModule>()
            launchModules(launchableModules, ctx, resolver)

            Either.right(resolver)
        } catch (th: Throwable) {
            Either.left(th)
        }
    }
}
