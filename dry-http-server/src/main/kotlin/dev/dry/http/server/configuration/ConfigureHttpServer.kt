package dev.dry.http.server.configuration

import dev.dry.configuration.properties.ConfigurationProperties
import dev.dry.dependency.DependencyRegistry
import dev.dry.http.server.parameter.AuthenticatedPrincipalFactory
import dev.dry.http.server.parameter.AuthenticatedPrincipalFactory.JwtBearerTokenAuthenticatedPrincipalFactory

object ConfigureHttpServer {
    operator fun invoke(configurationProperties: ConfigurationProperties, registry: DependencyRegistry) {
        registry.singleton<AuthenticatedPrincipalFactory> { JwtBearerTokenAuthenticatedPrincipalFactory(resolve()) }
    }
}
