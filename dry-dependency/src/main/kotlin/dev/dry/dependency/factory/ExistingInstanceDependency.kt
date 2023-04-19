package dev.dry.dependency.factory

import dev.dry.dependency.DependencyResolver
import kotlin.reflect.KClass

class ExistingInstanceDependency<T: Any>(
    name: String,
    type: KClass<T>,
    typeName: String,
    private val instance: T,
) : AbstractDependency<T>(name, type, typeName) {
    override fun instance(resolver: DependencyResolver): T = instance
}
