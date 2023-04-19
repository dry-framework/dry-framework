package dev.dry.common.text.message

class StaticParameterisedMessage(val text: String): ParameterisedMessage {
    override fun toString(parameterResolver: ParameterValueResolver): String = text
}
