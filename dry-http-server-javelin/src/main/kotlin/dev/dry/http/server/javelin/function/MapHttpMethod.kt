package dev.dry.http.server.javelin.function

import dev.dry.http.Method
import dev.dry.http.Method.DELETE
import dev.dry.http.Method.GET
import dev.dry.http.Method.HEAD
import dev.dry.http.Method.OPTIONS
import dev.dry.http.Method.PATCH
import dev.dry.http.Method.POST
import dev.dry.http.Method.PUT
import dev.dry.http.Method.TRACE
import io.javalin.http.HandlerType

object MapHttpMethod {
    operator fun invoke(method: Method): HandlerType {
        return when(method) {
            GET -> HandlerType.GET
            POST -> HandlerType.POST
            PUT -> HandlerType.PUT
            PATCH -> HandlerType.PATCH
            DELETE -> HandlerType.DELETE
            OPTIONS -> HandlerType.OPTIONS
            TRACE -> HandlerType.TRACE
            HEAD -> HandlerType.HEAD
        }
    }
}