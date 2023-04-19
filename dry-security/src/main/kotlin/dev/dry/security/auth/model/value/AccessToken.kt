package dev.dry.security.auth.model.value

@JvmInline
value class AccessToken(val value: String) {
    companion object {
        fun from(encodedJwt: EncodedJwt): AccessToken = AccessToken(encodedJwt.value)
    }
}
