package dev.dry.security.auth


interface UsernamePasswordAuthenticationToken : AuthenticationToken {
    val username: String
    val password: String
}
