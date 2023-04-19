package dev.dry.qrcode.kotlin

import dev.dry.qrcode.QRCodeContent
import dev.dry.qrcode.QRCodeGenerator
import io.github.g0dkar.qrcode.QRCode
import java.io.OutputStream

class DefaultQRCodeGenerator : QRCodeGenerator {
    override fun generate(content: QRCodeContent, outputStream: OutputStream) {
        QRCode(content.toString()).render().writeImage(outputStream)
    }
}
