package dev.dry.jwt

import dev.dry.security.auth.model.value.EncodedJwt

interface JwtCreator {
    fun create(jwt: Jwt): EncodedJwt
}
