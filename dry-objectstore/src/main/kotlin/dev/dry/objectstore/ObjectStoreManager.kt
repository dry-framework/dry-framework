package dev.dry.objectstore

interface ObjectStoreManager {
    fun exists(objectStoreName: ObjectStoreName): Boolean

    fun getOrNull(objectStoreName: ObjectStoreName): ObjectStore?

    fun get(objectStoreName: ObjectStoreName): ObjectStore

    fun list(): List<ObjectStoreName>

    fun create(objectStoreName: ObjectStoreName): ObjectStore
}
