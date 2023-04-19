package dev.dry.common.error

class ErrorCodeRanges(private val group: ErrorGroup) {
    companion object {
        private val CODE_RANGE = 1..999
    }

    private val rangeByCategory = ErrorCategory.values()
        .map { ErrorCodeRange(it, group, CODE_RANGE) }
        .associateBy { it.category }

    fun get(category: ErrorCategory): ErrorCodeRange {
        return rangeByCategory[category] ?: throw IllegalArgumentException("error code range not defined for category")
    }

    fun badRequest(): ErrorCodeRange = get(ErrorCategory.BAD_REQUEST)

    fun unauthorized(): ErrorCodeRange = get(ErrorCategory.UNAUTHORIZED)

    fun forbidden(): ErrorCodeRange = get(ErrorCategory.FORBIDDEN)

    fun notFound(): ErrorCodeRange = get(ErrorCategory.NOT_FOUND)

    fun conflict(): ErrorCodeRange = get(ErrorCategory.CONFLICT)

    fun unsupportedMediaType(): ErrorCodeRange = get(ErrorCategory.UNSUPPORTED_MEDIA_TYPE)

    fun server(): ErrorCodeRange = get(ErrorCategory.SERVER)
}
