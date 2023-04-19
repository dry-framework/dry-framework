package dev.dry.common.text.message

import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class ObjectPropertyParameterValueResolver<T: Any>(private val obj: T): ParameterValueResolver {
    private val propertyByName: Map<String, KProperty1<T, *>>

    init {
        @Suppress("UNCHECKED_CAST")
        val memberProperties = obj::class.memberProperties as Collection<KProperty1<T, *>>
        propertyByName = memberProperties.associateBy { it.name }
    }

    override fun resolveOrNull(name: String): Any? {
        return propertyByName[name]?.get(obj)
    }
}
