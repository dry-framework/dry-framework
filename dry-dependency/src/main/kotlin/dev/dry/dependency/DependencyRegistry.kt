package dev.dry.dependency

import dev.dry.dependency.exception.DependencyRegistryException
import dev.dry.dependency.factory.ExistingInstanceDependency
import dev.dry.dependency.factory.MultipleInstanceDependency
import dev.dry.dependency.factory.SingletonDependency
import kotlin.reflect.KClass

class DependencyRegistry(private val nameResolver: DependencyNameResolver) {
    private class DependencyEntry<T: Any>(dependency: Dependency<T>) {
        val name: String = dependency.name
        val type: KClass<T> = dependency.type
        val typeName: String = dependency.typeName
        val dependencies: MutableList<Dependency<T>> = mutableListOf(dependency)

        fun addDependency(dependency: Dependency<T>): DependencyEntry<T> {
            dependencies.add(dependency)
            return this
        }

        fun toSingleDependency(): Dependency<T> {
            return if (dependencies.size > 1) {
                MultipleInstanceDependency(name, type, typeName, dependencies.toList())
            } else {
                dependencies.first()
            }
        }
    }

    private val dependencyEntryByName: MutableMap<String, DependencyEntry<*>> = mutableMapOf()

    private fun <T: Any> registerDependency(dependency: Dependency<T>) {
        dependencyEntryByName.compute(dependency.name) { _, entry ->
            if (entry == null) {
                DependencyEntry(dependency)
            } else {
                if (!dependency.isCompatibleWithType(entry.type)) {
                    throw DependencyRegistryException(
                        "attempt to add multiple dependencies with name '${dependency.name}' and " +
                            "incompatible types -- '${dependency.typeName}' is not compatible with '${entry.typeName}'"
                    )
                }
                @Suppress("UNCHECKED_CAST")
                (entry as DependencyEntry<T>).addDependency(dependency)
            }
        }
    }

    fun <TYPE : Any, IMPLEMENTATION : TYPE> instance(
        type: KClass<TYPE>,
        instance: IMPLEMENTATION
    ) {
        val name = nameResolver.resolveName(type)
        val typeName = nameResolver.resolveTypeName(type)
        registerDependency(ExistingInstanceDependency(name, type, typeName, instance))
    }

    inline fun <reified TYPE: Any> instance(instance: TYPE) = instance(TYPE::class, instance)

    fun <TYPE : Any, IMPLEMENTATION : TYPE> singleton(
        type: KClass<TYPE>,
        construct: DependencyResolver.() -> IMPLEMENTATION
    ) {
        val name = nameResolver.resolveName(type)
        val typeName = nameResolver.resolveTypeName(type)
        registerDependency(SingletonDependency(name, type, typeName, construct))
    }

    inline fun <reified TYPE: Any> singleton(
        noinline construct: DependencyResolver.() -> TYPE
    ) = singleton(TYPE::class, construct)

    fun toResolver(): DependencyResolver {
        val dependencyByName = dependencyEntryByName.values
            .map { it.toSingleDependency() }
            .associateBy { it.name }
        return DependencyResolver(nameResolver, dependencyByName)
    }
}
