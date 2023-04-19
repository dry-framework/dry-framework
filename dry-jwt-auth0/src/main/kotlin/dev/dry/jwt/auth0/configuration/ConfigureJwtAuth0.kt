package dev.dry.jwt.auth0.configuration

import dev.dry.configuration.properties.ConfigurationProperties
import dev.dry.dependency.DependencyRegistry
import dev.dry.jwt.JwtCreator
import dev.dry.jwt.JwtVerifier
import dev.dry.jwt.auth0.Auth0JwtCreator
import dev.dry.jwt.auth0.Auth0JwtVerifier

object ConfigureJwtAuth0 {
    operator fun invoke(properties: ConfigurationProperties, registry: DependencyRegistry) {
        val provider = { "LdaxaAyb2cpSuEvzuy37pBTpuqg7o9LksOQxXbyC8Us" } // TODO("load from configuration")
        registry.instance<JwtCreator>(Auth0JwtCreator(provider))
        registry.instance<JwtVerifier>(Auth0JwtVerifier(provider))
    }
}
