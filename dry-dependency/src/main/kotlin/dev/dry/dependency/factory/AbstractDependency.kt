package dev.dry.dependency.factory

import dev.dry.dependency.Dependency
import kotlin.reflect.KClass

abstract class AbstractDependency<T: Any>(
    override val name: String,
    override val type: KClass<T>,
    override val typeName: String,
) : Dependency<T>

