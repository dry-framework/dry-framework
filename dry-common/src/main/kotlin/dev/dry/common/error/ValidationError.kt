package dev.dry.common.error

data class ValidationError(
    override val code: ErrorCode,
    override val message: String,
    val validationErrorMessages: List<ValidationErrorMessage>,
) : CodedError {
    override val exceptionMessage: String? get() = null
    override val cause: Throwable? get() = null

    data class ValidationErrorMessage(val name: String, val message: String)
}