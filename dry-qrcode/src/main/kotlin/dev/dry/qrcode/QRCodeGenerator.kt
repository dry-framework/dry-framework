package dev.dry.qrcode

import java.io.ByteArrayOutputStream
import java.io.OutputStream

interface QRCodeGenerator {
    fun generate(content: QRCodeContent, outputStream: OutputStream)

    fun generate(content: QRCodeContent): QRCodeImage {
        val outputStream = ByteArrayOutputStream()
        generate(content, outputStream)
        return QRCodeImage(outputStream.toByteArray())
    }
}
