package dev.dry.objectstore.file

import dev.dry.objectstore.ObjectName
import dev.dry.objectstore.ObjectOperationName.DELETE
import dev.dry.objectstore.ObjectOperationName.GET
import dev.dry.objectstore.ObjectOperationName.LIST
import dev.dry.objectstore.ObjectOperationName.PUT
import dev.dry.objectstore.ObjectStore
import dev.dry.objectstore.ObjectStoreName
import dev.dry.objectstore.exception.ObjectOperationFailedException
import java.io.FileFilter
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path
import kotlin.io.path.exists

class FileObjectStore(
    override val name: ObjectStoreName,
    private val basePath: Path,
) : ObjectStore {
    private fun resolvePath(objectName: ObjectName): Path = basePath.resolve(objectName.value)

    @Throws(ObjectOperationFailedException::class)
    override fun exists(objectName: ObjectName): Boolean = resolvePath(objectName).exists()

    @Throws(ObjectOperationFailedException::class)
    override fun list(objectName: ObjectName): Iterable<ObjectName> {
        try {
            val file = resolvePath(objectName).toFile()
            if (!file.isDirectory) {
                return emptyList()
            }
            return file.listFiles(FileFilter { !it.isHidden })
                ?.map { ObjectName("${objectName.value}/${it.name}") }
                ?: emptyList()
        } catch (ex: Exception) {
            throw ObjectOperationFailedException(LIST, objectName, name, ex)
        }
    }

    @Throws(ObjectOperationFailedException::class)
    override fun delete(objectName: ObjectName) {
        try {
            val file = resolvePath(objectName).toFile()
            if (file.isDirectory) {
                file.deleteRecursively()
            } else {
                file.delete()
            }
        } catch (ex: Exception) {
            throw ObjectOperationFailedException(DELETE, objectName, name, ex)
        }
    }

    @Throws(ObjectOperationFailedException::class)
    override fun get(objectName: ObjectName, outputStream: OutputStream) {
        try {
            resolvePath(objectName).toFile().inputStream().use { it.copyTo(outputStream) }
        } catch(ex: Exception) {
            throw ObjectOperationFailedException(GET, objectName, name, ex)
        }
    }

    @Throws(ObjectOperationFailedException::class)
    override fun put(objectName: ObjectName, inputStream: InputStream) {
        try {
            val file = resolvePath(objectName).toFile()
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }

            file.outputStream().use(inputStream::copyTo)
        } catch(ex: Exception) {
            throw ObjectOperationFailedException(PUT, objectName, name, ex)
        }
    }
}
