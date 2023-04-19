package dev.dry.objectstore.exception

sealed class ObjectStoreException(
    message: String? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause)