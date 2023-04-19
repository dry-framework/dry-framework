package dev.dry.dependency.exception

class DependencyResolutionFailedException(
    name: String,
    typeName: String,
    reason: String,
) : DependencyException(message(name, typeName, reason)) {
    companion object {
        private fun message(
            name: String,
            typeName: String,
            reason: String,
        ): String {
            return "failed to resolve dependency with name '$name' and type '$typeName' -- $reason"
        }
    }
}
