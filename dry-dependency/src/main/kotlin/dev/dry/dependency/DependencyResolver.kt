package dev.dry.dependency

import dev.dry.dependency.exception.DependencyNotFoundException
import dev.dry.dependency.exception.DependencyResolutionFailedException
import kotlin.reflect.KClass


open class DependencyResolver(
    private val nameResolver: DependencyNameResolver,
    private val dependencyByName: Map<String, Dependency<*>>
) {
    fun <T : Any> resolveAllOrEmpty(name: String, type: KClass<T>): List<T> {
        val dependency = dependencyByName[name]
        return if (dependency != null) {
            if (dependency.isCompatibleWithType(type)) {
                @Suppress("UNCHECKED_CAST")
                dependency.allInstances(this) as List<T>
            } else {
                val typeName = nameResolver.resolveTypeName(type)
                throw DependencyResolutionFailedException(
                    name,
                    typeName,
                    "request type '$typeName' is incompatible with '${dependency.typeName}'"
                )
            }
        } else {
            emptyList()
        }
    }

    fun <T : Any> resolveAll(name: String, type: KClass<T>): List<T> {
        return resolveAllOrEmpty(name, type).ifEmpty { throw DependencyNotFoundException(name) }
    }

    fun <T : Any> resolveAll(type: KClass<T>): List<T> = resolveAll(nameResolver.resolveName(type), type)

    inline fun <reified T : Any> resolveAll(): List<T> = resolveAll(T::class)

    fun <T : Any> resolveOrNull(name: String, type: KClass<T>): T? {
        val instances = resolveAllOrEmpty(name, type)
        if (instances.size > 1) {
            throw DependencyResolutionFailedException(
                name,
                nameResolver.resolveTypeName(type),
                "${instances.size} instances resolved but expected 1"
            )
        }
        return instances.firstOrNull()
    }

    fun <T : Any> resolveOrNull(type: KClass<T>): T? = resolveOrNull(nameResolver.resolveName(type), type)

    inline fun <reified T : Any> resolveOrNull(name: String): T? = resolveOrNull(name, T::class)

    inline fun <reified T : Any> resolveOrNull(): T? = resolveOrNull(T::class)

    fun <T : Any> resolve(name: String, type: KClass<T>): T {
        return resolveOrNull(name, type) ?: throw DependencyResolutionFailedException(
            name,
            nameResolver.resolveTypeName(type),
            "no instances resolved"
        )
    }

    fun <T : Any> resolve(type: KClass<T>): T = resolve(nameResolver.resolveName(type), type)

    inline fun <reified T : Any> resolve(name: String): T = resolve(name, T::class)

    inline fun <reified T : Any> resolve(): T = resolve(T::class)
}
