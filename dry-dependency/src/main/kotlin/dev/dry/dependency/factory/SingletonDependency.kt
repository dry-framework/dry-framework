package dev.dry.dependency.factory

import dev.dry.dependency.DependencyResolver
import kotlin.reflect.KClass

/**
 * instantiated lazily on the first access and re-used on all further requests
 */
class SingletonDependency<T: Any>(
    name: String,
    type: KClass<T>,
    typeName: String,
    private val instantiate: DependencyResolver.() -> T,
) : AbstractDependency<T>(name, type, typeName) {
    private var instance: T? = null

    override fun instance(resolver: DependencyResolver): T {
        return instance ?: synchronized(this) {
            instance ?: instantiate(resolver).also { instance = it }
        }
    }
}
