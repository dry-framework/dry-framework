package dev.dry.configuration

class ActiveProfile private constructor(val priority: Int, val name: String) {
    companion object {
        private val instanceByName = mutableMapOf<String, dev.dry.configuration.ActiveProfile>()

        fun from(priority: Int, name: String): dev.dry.configuration.ActiveProfile = dev.dry.configuration.ActiveProfile.Companion.instanceByName.computeIfAbsent(name) {
            dev.dry.configuration.ActiveProfile(priority, name)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as dev.dry.configuration.ActiveProfile

        if (name != other.name) return false
        if (priority != other.priority) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + priority
        return result
    }
}
