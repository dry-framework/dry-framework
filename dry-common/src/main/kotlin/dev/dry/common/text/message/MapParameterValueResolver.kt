package dev.dry.common.text.message

class MapParameterValueResolver(private val parameterMap: Map<String, Any>): ParameterValueResolver {
    companion object {
        fun parameters(vararg messageParameters: Pair<String, Any>): ParameterValueResolver {
            return MapParameterValueResolver(mapOf(*messageParameters))
        }
    }
    override fun resolveOrNull(name: String): Any? = parameterMap[name]
}
