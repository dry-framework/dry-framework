package dev.dry.common.text.message

interface ParameterisedMessage: Message {
    fun toString(parameterResolver: ParameterValueResolver): String
}
