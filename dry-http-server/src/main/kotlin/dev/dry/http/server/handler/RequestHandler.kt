package dev.dry.http.server.handler

import dev.dry.http.server.Request
import dev.dry.http.server.Response

fun interface RequestHandler {
    operator fun invoke(request: Request): Response
}
