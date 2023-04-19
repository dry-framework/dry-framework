package dev.dry.objectstore.exception

import dev.dry.objectstore.ObjectStoreName

class ObjectStoreAlreadyExistsException(
    objectStoreName: ObjectStoreName,
    cause: Throwable? = null
) : ObjectStoreException(
    "object store already exists with name '${objectStoreName}'",
    cause,
)
