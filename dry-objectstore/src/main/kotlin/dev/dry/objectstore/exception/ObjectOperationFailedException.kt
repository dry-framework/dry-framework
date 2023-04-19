package dev.dry.objectstore.exception

import dev.dry.objectstore.ObjectName
import dev.dry.objectstore.ObjectOperationName
import dev.dry.objectstore.ObjectStoreName

class ObjectOperationFailedException(
    operationName: ObjectOperationName,
    objectName: ObjectName,
    objectStoreName: ObjectStoreName,
    cause: Throwable? = null,
) : ObjectException(
    objectName,
    objectStoreName,
    "'${operationName}' operation failed on object with name '${objectName}' in store '${objectStoreName}'",
    cause,
)
