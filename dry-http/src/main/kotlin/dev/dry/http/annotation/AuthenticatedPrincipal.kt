package dev.dry.http.annotation

import kotlin.annotation.AnnotationRetention.RUNTIME

@Retention(RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class AuthenticatedPrincipal(val required: Boolean = true)
