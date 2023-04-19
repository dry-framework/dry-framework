package dev.dry.application.module

import dev.dry.application.launcher.ApplicationLaunchContext
import dev.dry.configuration.resource.ConfigurationResourceLoader
import dev.dry.dependency.DependencyRegistry
import dev.dry.dependency.DependencyResolver

interface ApplicationModule

interface ConfigurationResourceLoaderModule: ApplicationModule {
    fun configurationResourceLoader(): ConfigurationResourceLoader
}

interface ConfigurableModule: ApplicationModule {
    fun configure(ctx: ApplicationLaunchContext, registry: DependencyRegistry)
}

interface LaunchableModule: ApplicationModule {
    fun launch(ctx: ApplicationLaunchContext, resolver: DependencyResolver)
}
