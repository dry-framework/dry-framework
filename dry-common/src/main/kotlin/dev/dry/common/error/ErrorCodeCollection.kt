package dev.dry.common.error

abstract class ErrorCodeCollection(group: ErrorGroup) : Iterable<ErrorCode> {
    constructor(groupNumber: Int, description: String) : this(ErrorGroup.from(groupNumber, description))

    private val ranges = ErrorCodeRanges(group)

    protected val badRequest: ErrorCodeRange = ranges.badRequest()

    protected val unauthorized: ErrorCodeRange = ranges.unauthorized()

    protected val forbidden: ErrorCodeRange = ranges.forbidden()

    protected val notFound: ErrorCodeRange = ranges.notFound()

    protected val conflict: ErrorCodeRange = ranges.conflict()

    protected val unsupportedMediaType: ErrorCodeRange = ranges.unsupportedMediaType()

    protected val server: ErrorCodeRange = ranges.server()

     override fun iterator(): Iterator<ErrorCode> {
        val errorCodes = badRequest.errorCodes +
            unauthorized.errorCodes +
            forbidden.errorCodes +
            notFound.errorCodes +
            conflict.errorCodes +
            server.errorCodes
         return errorCodes.iterator()
     }
 }
