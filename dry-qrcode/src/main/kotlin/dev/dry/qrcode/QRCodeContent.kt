package dev.dry.qrcode

@JvmInline
value class QRCodeContent(val value: String) {
    companion object {
        fun builder(rootTag: String): QRCodeContentBuilder = QRCodeContentBuilder(rootTag)
    }

    override fun toString(): String = value
}
