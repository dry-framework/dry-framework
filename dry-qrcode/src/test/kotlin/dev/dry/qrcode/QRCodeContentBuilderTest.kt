package dev.dry.qrcode

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class QRCodeContentBuilderTest {
    @Test
    fun build() {
        val content = QRCodeContentBuilder("A")
            .tag("B", "1")
            .tag("C", "2")
            .tag("D", "3")
            .build()
        assertEquals("A:B:1;C:2;D:3;;", content.value)
        assertEquals("A:B:1;C:2;D:3;;", content.toString())
    }
}