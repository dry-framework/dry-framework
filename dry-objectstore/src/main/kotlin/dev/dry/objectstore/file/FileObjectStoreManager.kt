package dev.dry.objectstore.file

import dev.dry.objectstore.ObjectStore
import dev.dry.objectstore.ObjectStoreManager
import dev.dry.objectstore.ObjectStoreName
import dev.dry.objectstore.ObjectStoreOperationName.CREATE
import dev.dry.objectstore.ObjectStoreOperationName.GET
import dev.dry.objectstore.ObjectStoreOperationName.LIST
import dev.dry.objectstore.exception.ObjectStoreAlreadyExistsException
import dev.dry.objectstore.exception.ObjectStoreNotFoundException
import dev.dry.objectstore.exception.ObjectStoreOperationFailedException
import java.io.FileFilter
import java.nio.file.Path
import kotlin.io.path.createDirectory

class FileObjectStoreManager(
    private val basePath: Path,
) : ObjectStoreManager {
    override fun exists(objectStoreName: ObjectStoreName): Boolean {
        return basePath.resolve(objectStoreName.value).toFile().exists()
    }

    override fun getOrNull(objectStoreName: ObjectStoreName): ObjectStore? {
        try {
            return if (exists(objectStoreName)) {
                FileObjectStore(objectStoreName, basePath.resolve(objectStoreName.value))
            } else null
        } catch(ex: Exception) {
            throw ObjectStoreOperationFailedException(GET, objectStoreName, ex)
        }
    }

    override fun get(objectStoreName: ObjectStoreName): ObjectStore {
        return getOrNull(objectStoreName) ?: throw ObjectStoreNotFoundException(objectStoreName)
    }

    override fun list(): List<ObjectStoreName> {
        try {
            return basePath.toFile().listFiles(FileFilter { it.isDirectory && !it.isHidden })
                ?.map { ObjectStoreName(it.name) } ?: throw ObjectStoreOperationFailedException(LIST)
        } catch(ex: Exception) {
            throw ObjectStoreOperationFailedException(LIST, ex)
        }
    }

    override fun create(objectStoreName: ObjectStoreName): ObjectStore {
        if (exists(objectStoreName)) {
            throw ObjectStoreAlreadyExistsException(objectStoreName)
        }

        try {
            val objectStoreDirectory = basePath.resolve(objectStoreName.value).createDirectory()
            return FileObjectStore(objectStoreName, objectStoreDirectory.toAbsolutePath())
        } catch(ex: Exception) {
            throw ObjectStoreOperationFailedException(CREATE, objectStoreName, ex)
        }
    }
}
