package dev.dry.objectstore.exception

import dev.dry.objectstore.ObjectStoreName
import dev.dry.objectstore.ObjectStoreOperationName

class ObjectStoreOperationFailedException private constructor(
    message: String,
    cause: Throwable? = null,
) : ObjectStoreException(message, cause) {
    constructor(
        operationName: ObjectStoreOperationName,
        objectStoreName: ObjectStoreName,
        cause: Throwable? = null,
    ) : this(
        "'${operationName}' operation failed on object store with name '${objectStoreName}'",
        cause,
    )

    constructor(
        operationName: ObjectStoreOperationName,
        cause: Throwable? = null,
    ) : this(
        "'${operationName}' operation failed",
        cause,
    )
}
