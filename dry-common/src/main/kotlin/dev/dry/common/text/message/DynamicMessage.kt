package dev.dry.common.text.message

class DynamicMessage(
    private val generator: (parameterResolver: ParameterValueResolver) -> Any
): ParameterisedMessage {
    override fun toString(parameterResolver: ParameterValueResolver): String {
        return generator(parameterResolver).toString()
    }
}
