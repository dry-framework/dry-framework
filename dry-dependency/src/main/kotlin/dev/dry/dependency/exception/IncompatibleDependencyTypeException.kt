package dev.dry.dependency.exception

import dev.dry.dependency.Dependency

class IncompatibleDependencyTypeException(
    dependency: Dependency<*>,
    typeName: String,
) : DependencyException(message(dependency, typeName)) {
    companion object {
        private fun message(
            dependency: Dependency<*>,
            typeName: String,
        ): String {
            return "dependency with name '${dependency.name}' and type '${dependency.typeName}' " +
                    "is incompatible with type '$typeName'"
        }
    }
}
