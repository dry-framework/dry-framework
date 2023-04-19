package dev.dry.dependency.factory

import dev.dry.dependency.Dependency
import dev.dry.dependency.DependencyResolver
import dev.dry.dependency.exception.DependencyResolutionFailedException
import kotlin.reflect.KClass

class MultipleInstanceDependency<T: Any>(
    name: String,
    type: KClass<T>,
    typeName: String,
    private val dependencies: List<Dependency<T>>,
) : AbstractDependency<T>(name, type, typeName) {
    override fun instance(resolver: DependencyResolver): T {
        val size = dependencies.size
        return if (size == 1) {
            val dependency = dependencies.first()
            dependency.instance(resolver)
        } else {
            throw DependencyResolutionFailedException(
                name,
                typeName,
                "resolved $size instances but expected 1"
            )
        }
    }

    override fun allInstances(resolver: DependencyResolver): List<T> {
        return dependencies.map { dependency -> dependency.instance(resolver) }
    }
}
