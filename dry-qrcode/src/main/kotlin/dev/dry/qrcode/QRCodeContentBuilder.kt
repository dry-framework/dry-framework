package dev.dry.qrcode

class QRCodeContentBuilder(rootTag: String) {
    companion object {
        const val TAG_VALUE_DELIMITER = ":"
        const val TAG_VALUE_TERMINATOR = ";"
    }

    private val buffer: StringBuilder = StringBuilder().apply {
        append(rootTag)
        append(TAG_VALUE_DELIMITER)
        ensureCapacity(256)
    }

    fun tag(tag: String, value: Any): QRCodeContentBuilder {
        buffer
            .append(tag)
            .append(TAG_VALUE_DELIMITER)
            .append(value)
            .append(TAG_VALUE_TERMINATOR)
        return this
    }

    fun build(): QRCodeContent {
        buffer.append(TAG_VALUE_TERMINATOR)
        return QRCodeContent(buffer.toString())
    }
}
