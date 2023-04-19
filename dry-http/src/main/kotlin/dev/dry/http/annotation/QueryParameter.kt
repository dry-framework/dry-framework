package dev.dry.http.annotation

import kotlin.annotation.AnnotationRetention.RUNTIME

@Retention(RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class QueryParameter(
    val name: String = "",
    val required: Boolean = true,
    val defaultValue: String = "",
)
