package dev.dry.objectstore

import dev.dry.common.error.CodedError
import dev.dry.common.error.ErrorCodeCollection
import dev.dry.common.error.toError
import dev.dry.common.text.message.DynamicMessage
import dev.dry.common.text.message.MapParameterValueResolver.Companion.parameters

object ObjectStoreErrors : ErrorCodeCollection(1, "object-store") {
    val INVALID_OBJECT_STORE_NAME = badRequest.get(0, "invalid object store name").toError()
    val OBJECT_STORE_ALREADY_EXISTS = server.get(
        1,
        "object store already exists",
        DynamicMessage {"object store already exists with name '${it["name"]}'" },
    )
    val OBJECT_STORE_OPERATION_FAILED = server.get(
        2,
        "object store operation failed",
        DynamicMessage { "object store '${it["operation"]}' failed" },
        DynamicMessage { "object store '${it["operation"]}' failed -- '${it["cause"]}'" },
    )
    val INVALID_OBJECT_NAME = badRequest.get(3, "invalid object name").toError()
    private val OBJECT_ALREADY_EXISTS = server.get(
        4,
        "object already exists",
        DynamicMessage { "object already exists with name '${it["name"]}'" }
    )
    fun objectAlreadyExists(objectName: ObjectName): CodedError {
        return OBJECT_ALREADY_EXISTS.toError(parameters("name" to objectName.value))
    }
    val OBJECT_OPERATION_FAILED = server.get(
        5,
        "object operation failed",
        DynamicMessage {"object '${it["operation"]}' failed" },
        DynamicMessage {"object '${it["operation"]}' failed -- '${it["cause"]}'" },
    )
}
