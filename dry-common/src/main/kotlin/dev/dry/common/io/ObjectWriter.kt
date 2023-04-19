package dev.dry.common.io

import java.io.OutputStream

interface ObjectWriter {
    fun <T: Any> write(output: OutputStream, instance: T)
}
