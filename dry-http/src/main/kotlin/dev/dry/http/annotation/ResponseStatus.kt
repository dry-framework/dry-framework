package dev.dry.http.annotation

import dev.dry.http.Status
import kotlin.annotation.AnnotationRetention.RUNTIME

@Retention(RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class ResponseStatus(val value: Status)
