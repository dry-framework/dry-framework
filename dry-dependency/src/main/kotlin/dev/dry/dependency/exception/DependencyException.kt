package dev.dry.dependency.exception

import dev.dry.common.exception.SystemRuntimeException

sealed class DependencyException(message: String) : SystemRuntimeException(message)
