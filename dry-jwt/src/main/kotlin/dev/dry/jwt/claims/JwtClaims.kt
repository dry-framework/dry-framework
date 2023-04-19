package dev.dry.jwt.claims

import dev.dry.jwt.Jwt
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface JwtClaims {
    companion object {
        private const val NAMESPACE = "https://dev.dry"
        const val EMAIL = "$NAMESPACE/email"
        const val ROLES = "$NAMESPACE/roles"
        const val LAST_LOGIN = "$NAMESPACE/lastLogin"

        fun rolesToClaim(roles: List<String>): StringArrayJwtClaim = StringArrayJwtClaim(roles)

        @JvmStatic
        fun lastLoginToClaim(lastLogin: LocalDateTime?): StringJwtClaim? = lastLogin?.let {
            StringJwtClaim(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(lastLogin))
        }

        @JvmStatic
        fun lastLoginFromClaim(jwt: Jwt): LocalDateTime? {
            val lastLoginClaim = jwt.claims[LAST_LOGIN]
            return if (lastLoginClaim is StringJwtClaim) {
                LocalDateTime.parse(lastLoginClaim.value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            } else null
        }
    }
}
