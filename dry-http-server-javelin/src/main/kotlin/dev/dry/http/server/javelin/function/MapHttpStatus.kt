package dev.dry.http.server.javelin.function

import dev.dry.http.Status
import dev.dry.http.Status.BAD_REQUEST
import dev.dry.http.Status.CONFLICT
import dev.dry.http.Status.FORBIDDEN
import dev.dry.http.Status.NOT_FOUND
import dev.dry.http.Status.OK
import dev.dry.http.Status.SERVER
import dev.dry.http.Status.UNAUTHORIZED
import dev.dry.http.Status.UNSUPPORTED_MEDIA_TYPE
import io.javalin.http.HttpStatus

object MapHttpStatus {
    operator fun invoke(status: Status): HttpStatus {
        return when(status) {
            OK -> HttpStatus.OK
            BAD_REQUEST -> HttpStatus.BAD_REQUEST
            UNAUTHORIZED -> HttpStatus.UNAUTHORIZED
            FORBIDDEN -> HttpStatus.FORBIDDEN
            NOT_FOUND -> HttpStatus.NOT_FOUND
            CONFLICT -> HttpStatus.CONFLICT
            UNSUPPORTED_MEDIA_TYPE -> HttpStatus.UNSUPPORTED_MEDIA_TYPE
            SERVER -> HttpStatus.INTERNAL_SERVER_ERROR
        }
    }
}