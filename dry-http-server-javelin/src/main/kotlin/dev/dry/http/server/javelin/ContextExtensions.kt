package dev.dry.http.server.javelin

import dev.dry.http.server.Response
import dev.dry.http.server.javelin.JavelinRequestAdaptor.Companion.JAVELIN_REQUEST_ADAPTOR_ATTRIBUTE_KEY
import dev.dry.http.server.javelin.function.MapHttpStatus
import io.javalin.http.Context

fun Context.requestAdaptor(): JavelinRequestAdaptor {
    return attribute(JAVELIN_REQUEST_ADAPTOR_ATTRIBUTE_KEY) ?: JavelinRequestAdaptor(this)
        .also { attribute(JAVELIN_REQUEST_ADAPTOR_ATTRIBUTE_KEY, it) }
}

fun Context.response(response: Response) {
    status(MapHttpStatus(response.status)).result(response.bodyStream())
}
