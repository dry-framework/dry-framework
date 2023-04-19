package dev.dry.common.text.message

interface ParameterValueResolver {
    fun resolveOrNull(name: String): Any?

    fun resolve(name: String): String = (resolveOrNull(name) ?: "#{$name}").toString()

    fun resolve(name: String, defaultValue: Any): String = (resolveOrNull(name) ?: defaultValue).toString()

    operator fun get(name: String): String = resolve(name)
}
