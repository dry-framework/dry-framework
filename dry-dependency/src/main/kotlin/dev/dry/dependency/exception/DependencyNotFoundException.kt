package dev.dry.dependency.exception

class DependencyNotFoundException(dependencyName: String) : DependencyException(
    "dependency not found with name '$dependencyName'"
)
