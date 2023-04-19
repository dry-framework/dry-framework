package dev.dry.security.auth

import dev.dry.common.error.CodedError
import dev.dry.common.function.Either

fun interface ConstructPrincipal<T : Principal> {
    operator fun invoke(token: AuthenticationToken): Either<CodedError, T>?
}
