package dev.dry.http.annotation

import dev.dry.http.Method
import kotlin.annotation.AnnotationRetention.RUNTIME

@Retention(RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Operation(
    val method: Method,
    val path: String,
    /**
     * Unique string used to identify the operation. The id MUST be unique among all operations described in the API.
     * The operationId value is case-sensitive.
     */
    val operationId: String = "",
    /**
     * A short summary of what the operation does.
     */
    val summary: String = "",
    /**
     * A verbose explanation of the operation behavior. CommonMark syntax MAY be used for rich text representation.
     */
    val description: String = "",
)
