package dev.dry.http.server.function

import dev.dry.http.server.Request

object GetBearerToken {
    operator fun invoke(request: Request): String? = request.header("Authorization")
        ?.trim()
        ?.takeIf { it.startsWith("Bearer") }
        ?.substringAfter("Bearer")
        ?.trim()
}
