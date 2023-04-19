package dev.dry.objectstore

import dev.dry.objectstore.exception.ObjectOperationFailedException
import java.io.InputStream
import java.io.OutputStream

interface ObjectStore {
    val name: ObjectStoreName

    @Throws(ObjectOperationFailedException::class)
    fun exists(objectName: ObjectName): Boolean

    @Throws(ObjectOperationFailedException::class)
    fun list(objectName: ObjectName): Iterable<ObjectName>

    @Throws(ObjectOperationFailedException::class)
    fun delete(objectName: ObjectName)

    @Throws(ObjectOperationFailedException::class)
    fun get(objectName: ObjectName, outputStream: OutputStream)

    @Throws(ObjectOperationFailedException::class)
    fun put(objectName: ObjectName, inputStream: InputStream)
}
