package dev.dry.data.model.entity

import java.time.LocalDateTime

interface UserEntity {
    val id: UserId
    val displayName: String
    val createdAt: LocalDateTime
    val lastLoginAt: LocalDateTime?

    @JvmInline
    value class UserId(val value: Int) {
        companion object {
            val NULL = UserId(0)
        }

        override fun toString(): String = value.toString()
    }
}
