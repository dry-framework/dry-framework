package dev.dry.common.error

object ErrorCodeRegistry {
    private val groupByCode = mutableMapOf<Int, ErrorGroup>()
    private val codeByString = mutableMapOf<String, ErrorCode>()

    internal fun register(errorGroup: ErrorGroup) {
        groupByCode.put(errorGroup.groupNumber, errorGroup)?.let {
            throw IllegalStateException(
                "attempt to register error group '${errorGroup.groupNumber}' with name '${errorGroup.description}' " +
                        "while already registered with '${it.description}'"
            )
        }
    }

    internal fun register(errorCode: ErrorCode) {
        val errorCodeString = errorCode.toString()
        codeByString.put(errorCodeString, errorCode)?.let {
            throw IllegalStateException(
                "attempt to register error code '$errorCodeString' with description '${errorCode.description}' " +
                        "while already registered with '${it.description}'"
            )
        }
    }
}
