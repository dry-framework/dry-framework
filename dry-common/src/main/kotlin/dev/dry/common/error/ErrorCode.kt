package dev.dry.common.error

class ErrorCode private constructor(
    val category: ErrorCategory,
    val group: ErrorGroup,
    val errorNumber: Int,
    val description: String,
) {
    companion object {
        private val CODE_RANGE = 0..9999

        @JvmStatic
        fun isValidCode(value: Int): Boolean = value in CODE_RANGE

        fun from(category: ErrorCategory, group: ErrorGroup, errorNumber: Int, description: String): ErrorCode {
            val errorCode = ErrorCode(category, group, errorNumber, description)
            ErrorCodeRegistry.register(errorCode)
            return errorCode
        }
    }

    private val stringRepresentation: String

    init {
        assert(isValidCode(errorNumber)) { "error code must be in range $CODE_RANGE" }

        val categoryNumber = category.categoryNumber
        val groupNumber = group.groupNumber.toString().padStart(2, '0')
        val errorNumber = errorNumber.toString().padStart(3, '0')

        stringRepresentation = "E$categoryNumber-$groupNumber-$errorNumber"
    }

    override fun toString(): String = stringRepresentation

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ErrorCode

        if (category != other.category) return false
        if (group != other.group) return false
        if (errorNumber != other.errorNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = category.hashCode()
        result = 31 * result + group.hashCode()
        result = 31 * result + errorNumber
        return result
    }
}
