package dev.dry.http.annotation

import kotlin.annotation.AnnotationRetention.RUNTIME

@Retention(RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class PathVariable(
    val name: String = "",
    val required: Boolean = true,
)
