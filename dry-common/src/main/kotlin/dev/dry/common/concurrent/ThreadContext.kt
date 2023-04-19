package dev.dry.common.concurrent

import java.security.Principal
import kotlin.reflect.KClass


class ThreadContext(
    val traceId: TraceId,
    private val map: MutableMap<String, Any> = mutableMapOf(),
) {
    private var _principal: Any? = null

    val principal: Any? get() = _principal

    inline fun <reified T : Principal> principal(): T? {
        val p = principal
        return (p as? T) ?: if (p == null) {
            null
        } else {
            val principalClassName = T::class.qualifiedName
            val targetClassName = T::class.qualifiedName
            throw ClassCastException(
                "failed to cast principal of type '$principalClassName' to '$targetClassName'"
            )
        }
    }

    fun principal(principal: Any?) {
        _principal = principal
    }

    operator fun set(key: String, value: Any) {
        map[key] = value
    }

    operator fun get(key: String): Any? = map[key]

    fun <T: Any> attribute(key: String, type: KClass<T>): T? {
        val value = this[key] ?: return null
        @Suppress("UNCHECKED_CAST")
        return value as? T ?: throw IllegalArgumentException(
            "cannot cast thread context attribute '$key' to class '${type.qualifiedName}'"
        )
    }

    inline fun <reified T: Any> attribute(key: String): T? = attribute(key, T::class)

    interface Listener {
        fun afterCreate(ctx: ThreadContext) {}
        fun beforeClear(ctx: ThreadContext) {}
    }

    companion object {
        private val threadLocal = ThreadLocal<ThreadContext?>()

        private val listeners: MutableSet<Listener> = mutableSetOf()

        fun addListener(listener: Listener) {
            listeners += listener
        }

        fun removeListener(listener: Listener) {
            listeners -= listener
        }

        fun initialise(traceId: TraceId = TraceId.newInstance()): ThreadContext {
            return ThreadContext(traceId).also { ctx ->
                listeners.forEach { it.afterCreate(ctx) }
            }
        }

        val instance: ThreadContext
            get() {
                return threadLocal.get() ?: throw IllegalStateException("thread context not initialised")
            }

        fun clear() {
            val ctx = threadLocal.get()
            if (ctx != null) {
                listeners.forEach { it.beforeClear(ctx) }
            }
            threadLocal.remove()
        }
    }
}
