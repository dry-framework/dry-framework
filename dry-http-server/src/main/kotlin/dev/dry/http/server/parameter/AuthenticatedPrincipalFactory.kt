package dev.dry.http.server.parameter

import dev.dry.common.error.CodedError
import dev.dry.common.function.Either
import dev.dry.http.server.Request
import dev.dry.http.server.function.GetBearerToken
import dev.dry.security.auth.ConstructPrincipal
import dev.dry.security.auth.JwtAuthenticationToken
import dev.dry.security.auth.Principal

fun interface AuthenticatedPrincipalFactory {
    fun construct(request: Request): Either<CodedError, Principal?>

    class JwtBearerTokenAuthenticatedPrincipalFactory(
        private val constructPrincipal: ConstructPrincipal<*>,
    ) : AuthenticatedPrincipalFactory {
        override fun construct(request: Request): Either<CodedError, Principal?> {
            return GetBearerToken(request)
                ?.let { bearerToken -> constructPrincipal(JwtAuthenticationToken.from(bearerToken)) }
                ?: Either.right(null)
        }
    }
}
