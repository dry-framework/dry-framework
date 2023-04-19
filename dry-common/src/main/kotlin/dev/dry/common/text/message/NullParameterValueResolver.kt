package dev.dry.common.text.message

class NullParameterValueResolver: ParameterValueResolver {
    override fun resolveOrNull(name: String): Any? = null
}
