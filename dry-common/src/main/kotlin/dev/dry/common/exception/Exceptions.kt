package dev.dry.common.exception

object Exceptions {
    private val LINE_SEPARATOR = System.getProperty("line.separator")

    @JvmStatic
    fun getMessageChain(th: Throwable?, lineIndent: CharSequence? = null): String {
        return getMessageChain("", th, lineIndent)
    }

    @JvmStatic
    fun getMessageChain(message: String, th: Throwable?, lineIndent: CharSequence? = null): String {
        val sb = StringBuilder(message)
        appendMessage(th, sb, 0, lineIndent)
        return sb.toString()
    }

    private fun appendMessage(th: Throwable?, sb: StringBuilder, depth: Int, lineIndent: CharSequence?) {
        if (th != null) {
            if (depth > 0) {
                sb.append(", ")
            }

            if (lineIndent != null) {
                sb.append(LINE_SEPARATOR)
                if (lineIndent.isNotEmpty()) {
                    for (index in 0..depth) {
                        sb.append(lineIndent)
                    }
                }
            }

            sb.append("[")
                .append(th::class.java.name)
                .append(": '")
                .append(th.message)
                .append("'")

            if (th.stackTrace.isNotEmpty()) {
                val stackElement = th.stackTrace.first()
                sb.append(", ")
                    .append(stackElement.fileName)
                    .append(" (line: ")
                    .append(stackElement.lineNumber)
                    .append(", method: ")
                    .append(stackElement.methodName)
            }

            sb.append(")]")

            appendMessage(th.cause, sb, depth + 1, lineIndent)
        }
    }

    private val FATAL_EXCEPTIONS: List<Class<out Throwable>> = setOf(
        VirtualMachineError::class,
        ThreadDeath::class,
        LinkageError::class,
        InterruptedException::class,
    ).map { it.java }

    fun isFatal(th: Throwable): Boolean = FATAL_EXCEPTIONS.any { it.isAssignableFrom(th.javaClass) }
}
