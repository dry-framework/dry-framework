package dev.dry.http.annotation

import kotlin.annotation.AnnotationRetention.RUNTIME

@Retention(RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class RequiredRoles(val roles: Array<String>)
