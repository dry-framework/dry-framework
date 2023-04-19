package dev.dry.common.io.encoding

import java.nio.charset.Charset

object Base64 {
    fun encode(bytes: ByteArray): ByteArray = java.util.Base64.getEncoder().encode(bytes)
    fun encodeToString(bytes: ByteArray, charset: Charset = Charsets.UTF_8): String = String(encode(bytes), charset)

    fun decode(bytes: ByteArray): ByteArray = java.util.Base64.getDecoder().decode(bytes)
    fun decodeFromString(string: String, charset: Charset = Charsets.UTF_8): ByteArray =
        decode(string.toByteArray(charset))
}
