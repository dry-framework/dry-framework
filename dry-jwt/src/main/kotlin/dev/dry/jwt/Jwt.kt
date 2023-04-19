package dev.dry.jwt

import dev.dry.jwt.claims.JwtClaim
import java.time.LocalDateTime

class Jwt(
    /** "kid" */
    val keyId: String? = null,
    /** "iss" claim */
    val issuer: String,
    /** "sub" claim */
    val subject: String,
    /** "aud" */
    val audience: List<String>? = null,
    /** "nbf" */
    val notBefore: LocalDateTime? = null,
    /** "iat" claim */
    val issuedAt: LocalDateTime? = null,
    /** "exp" claim */
    val expiresAt: LocalDateTime? = null,
    /** custom roles claim */
    val claims: Map<String, JwtClaim>,
)
