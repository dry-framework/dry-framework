package dev.dry.dependency

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

interface Dependency<T: Any> {
    val name: String
    val type: KClass<T>
    val typeName: String
    fun instance(resolver: DependencyResolver): T
    fun allInstances(resolver: DependencyResolver): List<T> = listOf(instance(resolver))
    fun isCompatibleWithType(typeOrSubType: KClass<*>): Boolean = typeOrSubType.isSubclassOf(type)
}
