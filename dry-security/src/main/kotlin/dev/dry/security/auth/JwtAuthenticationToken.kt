package dev.dry.security.auth

import dev.dry.security.auth.model.value.EncodedJwt

class JwtAuthenticationToken(val encodedJwt: EncodedJwt) : AuthenticationToken {
    companion object {
        fun from(encodedJwt: String): JwtAuthenticationToken = JwtAuthenticationToken(EncodedJwt(encodedJwt))
    }
}
