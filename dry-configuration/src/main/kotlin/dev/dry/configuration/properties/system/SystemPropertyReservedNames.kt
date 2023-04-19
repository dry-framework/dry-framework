package dev.dry.configuration.properties.system

object SystemPropertyReservedNames {
    private val PREFIXES = setOf(
        "jdk.",
        "java.",
        "os.",
        "file.separator", // File separator ("/" on UNIX)
        "path.separator", // Path separator (":" on UNIX)
        "line.separator", // Line separator ("\n" on UNIX)
        "user.name", // User's account name
        "user.home", // User's home directory
        "user.dir", // User's current working directory
    )

    fun includes(name: String): Boolean = PREFIXES.any { prefix -> name.startsWith(prefix) }
}
