package dev.dry.security.auth

interface Principal {
    val name: String
    val roles: Set<String>
}
