package dev.dry.dependency

import kotlin.reflect.KClass

interface DependencyNameResolver {
    fun <T: Any> resolveName(dependencyType: KClass<T>): String {
        return dependencyType.qualifiedName ?: dependencyType.java.name
    }

    fun <T: Any> resolveTypeName(dependencyType: KClass<T>): String {
        return resolveName(dependencyType)
    }
}

object DefaultDependencyNameResolver : DependencyNameResolver
