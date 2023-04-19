package dev.dry.jwt.auth0

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.Claim
import dev.dry.common.error.CodedError
import dev.dry.common.error.toError
import dev.dry.common.function.Either
import dev.dry.common.time.DateTimeMapper
import dev.dry.jwt.Jwt
import dev.dry.jwt.JwtVerifier
import dev.dry.jwt.claims.JwtClaim
import dev.dry.jwt.claims.JwtClaims
import dev.dry.jwt.claims.StringArrayJwtClaim
import dev.dry.jwt.claims.StringJwtClaim
import dev.dry.jwt.error.JwtErrors
import dev.dry.security.auth.model.value.EncodedJwt

class Auth0JwtVerifier(private val algorithmSupplier: AlgorithmSupplier): JwtVerifier {
    constructor(secretProvider: () -> String): this(HMACAlgorithmSupplier(secretProvider))

    private val claimMapperByName = mapOf<String, (c: Claim) -> JwtClaim>(
        JwtClaims.ROLES to { c -> StringArrayJwtClaim(c.asArray(String::class.java)) },
        JwtClaims.LAST_LOGIN to { c -> StringJwtClaim(c.asString()) },
    )

    override fun verify(encodedJwt: EncodedJwt): Either<CodedError, Jwt> {
        return try {
            val decodedJwt = algorithmSupplier.verifier().verify(encodedJwt.value)
            val claims = mutableMapOf<String, JwtClaim>()
            claimMapperByName.forEach { entry ->
                val claim = decodedJwt.getClaim(entry.key)
                if (claim?.isNull == false) {
                    claims[entry.key] = entry.value(claim)
                }
            }

            val jwt = Jwt(
                keyId = decodedJwt.keyId,
                issuer = decodedJwt.issuer,
                subject = decodedJwt.subject,
                audience = decodedJwt.audience,
                notBefore = if (decodedJwt.notBefore != null) DateTimeMapper.toLocalDateTime(decodedJwt.notBefore) else null,
                issuedAt = if (decodedJwt.issuedAt != null) DateTimeMapper.toLocalDateTime(decodedJwt.issuedAt) else null,
                expiresAt = if (decodedJwt.expiresAt != null) DateTimeMapper.toLocalDateTime(decodedJwt.expiresAt) else null,
                claims = claims.toMap()
            )
            Either.right(jwt)
        } catch (ex: TokenExpiredException) {
            Either.left(JwtErrors.JWT_TOKEN_EXPIRED.toError(cause = ex))
        } catch (ex: JWTVerificationException) {
            Either.left(JwtErrors.JWT_TOKEN_INVALID.toError(cause = ex))
        }
    }
}