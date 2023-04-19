package dev.dry.common.time

import java.time.Clock

object UtcTimeProvider : TimeProvider {
    override val clock: Clock = Clock.systemUTC()
}
