package dev.dry.common.error

class ErrorGroup private constructor(
    val groupNumber: Int,
    val description: String,
) {
    companion object {
        fun from(groupNumber: Int, description: String): ErrorGroup {
            val group = ErrorGroup(groupNumber, description)
            ErrorCodeRegistry.register(group)
            return group
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ErrorGroup

        if (groupNumber != other.groupNumber) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = groupNumber
        result = 31 * result + description.hashCode()
        return result
    }
}
