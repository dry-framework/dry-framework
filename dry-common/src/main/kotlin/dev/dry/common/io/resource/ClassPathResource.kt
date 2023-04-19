package dev.dry.common.io.resource

import java.io.InputStream
import kotlin.reflect.KClass

class ClassPathResource(path: String, contextClass: Class<*>? = null, classLoader: ClassLoader? = null): Resource {
    constructor(
        path: String,
        contextClass: KClass<*>,
        classLoader: ClassLoader? = null
    ): this(path, contextClass.java, classLoader)

    companion object {
        private const val PATH_SEPARATOR = "/"
        private const val PACKAGE_SEPARATOR = "."

        private fun constructPath(resourcePath: String, contextClass: Class<*>?): String {
            val isAbsolutePath = resourcePath.startsWith(PATH_SEPARATOR)
            return if (isAbsolutePath || contextClass == null) {
                resourcePath.substring(1)
            } else {
                val packagePath = contextClass.packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR)
                "$packagePath$PATH_SEPARATOR$resourcePath"
            }
        }

        private fun resolveClassLoader(contextClass: Class<*>?, classLoader: ClassLoader?): ClassLoader {
            return contextClass?.classLoader
                ?: classLoader
                ?: threadClassLoader()
                ?: ClassPathResource::class.java.classLoader
        }

        private fun threadClassLoader(): ClassLoader? {
            return try {
                Thread.currentThread().contextClassLoader
            } catch (ex: Throwable) {
                // no-op
                null
            }
        }
    }

    private val path: String = constructPath(path, contextClass)
    private val classLoader: ClassLoader = resolveClassLoader(contextClass, classLoader)

    // ========================================================================
    // ---{ Resource }---
    // ========================================================================

    override val name: String get() = path

    override fun openStream(): InputStream? = classLoader.getResourceAsStream(path)
}
