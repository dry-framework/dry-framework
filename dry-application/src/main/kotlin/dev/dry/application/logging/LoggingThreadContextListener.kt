package dev.dry.application.logging

import dev.dry.common.concurrent.ThreadContext
import org.slf4j.MDC

object LoggingThreadContextListener: ThreadContext.Listener {
    private const val TRACE_ID = "traceId"

    override fun afterCreate(ctx: ThreadContext) {
        MDC.put(TRACE_ID, ctx.traceId.value)
    }

    override fun beforeClear(ctx: ThreadContext) {
        MDC.clear()
    }
}
