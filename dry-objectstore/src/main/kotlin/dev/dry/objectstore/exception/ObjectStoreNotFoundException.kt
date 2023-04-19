package dev.dry.objectstore.exception

import dev.dry.objectstore.ObjectStoreName

class ObjectStoreNotFoundException(
    objectStoreName: ObjectStoreName,
    cause: Throwable? = null
) : ObjectStoreException(
    "object store not found with name '${objectStoreName}'",
    cause,
)
