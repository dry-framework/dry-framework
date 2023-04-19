package dev.dry.common.io.resource

import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class FileResource(private val path: Path): Resource {
    constructor(path: String): this(Path.of(path))

    // ========================================================================
    // ---{ Resource }---
    // ========================================================================

    override val name: String get() = path.toString()

    override fun openStream(): InputStream? = Files.newInputStream(path, StandardOpenOption.READ)
}
