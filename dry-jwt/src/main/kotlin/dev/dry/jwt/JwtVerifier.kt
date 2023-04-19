package dev.dry.jwt

import dev.dry.common.error.CodedError
import dev.dry.common.function.Either
import dev.dry.security.auth.model.value.EncodedJwt

interface JwtVerifier {
    fun verify(jwtString: EncodedJwt): Either<CodedError, Jwt>
}
