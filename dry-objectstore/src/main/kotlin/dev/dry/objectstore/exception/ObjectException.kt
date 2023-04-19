package dev.dry.objectstore.exception

import dev.dry.objectstore.ObjectName
import dev.dry.objectstore.ObjectStoreName

sealed class ObjectException(
    val objectName: ObjectName,
    val objectStoreName: ObjectStoreName,
    message: String? = null,
    cause: Throwable? = null,
) : ObjectStoreException(message, cause)
