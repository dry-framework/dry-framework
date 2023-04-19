package dev.dry.jwt.auth0

import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTCreationException
import dev.dry.common.error.toException
import dev.dry.common.time.DateTimeMapper
import dev.dry.jwt.Jwt
import dev.dry.jwt.JwtCreator
import dev.dry.jwt.claims.StringArrayJwtClaim
import dev.dry.jwt.claims.StringJwtClaim
import dev.dry.jwt.error.JwtErrors
import dev.dry.security.auth.model.value.EncodedJwt
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

class Auth0JwtCreator(private val algorithmSupplier: AlgorithmSupplier): JwtCreator {
    constructor(secretProvider: () -> String): this(HMACAlgorithmSupplier(secretProvider))

    constructor(publicKey: RSAPublicKey, privateKey: RSAPrivateKey): this(RSAAlgorithmSupplier(publicKey, privateKey))

    override fun create(jwt: Jwt): EncodedJwt {
        return try {
            val issuedAt = jwt.issuedAt
            val expiresAt = jwt.expiresAt
            val builder = JWT.create()
                .withIssuer(jwt.issuer)
                .withSubject(jwt.subject)
                .withIssuedAt(if (issuedAt != null) DateTimeMapper.toDate(issuedAt) else null)
                .withExpiresAt(if (expiresAt != null) DateTimeMapper.toDate(expiresAt) else null)

            jwt.claims.forEach { e ->
                when(val claim = e.value) {
                    is StringArrayJwtClaim -> builder.withArrayClaim(e.key, claim.value)
                    is StringJwtClaim -> builder.withClaim(e.key, claim.value)
                }
            }

            EncodedJwt(builder.sign(algorithmSupplier.algorithm()))
        } catch (ex: JWTCreationException) {
            // Invalid Signing configuration / Couldn't convert Claims.
            throw JwtErrors.JWT_CREATION_FAILED.toException(cause = ex)
        }
    }
}
