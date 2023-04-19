package dev.dry.http.server

interface UriPath {
    override fun toString(): String

    companion object {
        operator fun invoke(value: String): UriPath = DefaultUriPath(value)
    }

    class DefaultUriPath(private val value: String) : UriPath{
        override fun toString(): String = value
    }
}
