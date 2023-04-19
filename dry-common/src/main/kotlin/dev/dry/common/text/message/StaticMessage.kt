package dev.dry.common.text.message

class StaticMessage(val text: String): Message {
    override fun toString(): String = text
}
