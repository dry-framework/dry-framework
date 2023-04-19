package dev.dry.common.text.message

import java.util.regex.*

class MessageFormat(format: String): ParameterisedMessage {
    companion object {
        val PLACEHOLDER_PATTERN: Pattern = Pattern.compile("#\\{([a-zA-Z0-9]*)}")
    }

    private val segments: List<String>
    private val placeholderIndices: Set<Int>

    init {
        val matcher = PLACEHOLDER_PATTERN.matcher(format)
        val segmentList = mutableListOf<String>()
        val placeholderIndexSet = mutableSetOf<Int>()
        var index = 0
        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()
            val placeholder = matcher.group(1)
            segmentList.add(format.substring(index, start))
            segmentList.add(placeholder)
            placeholderIndexSet.add(segmentList.size - 1)
            index = end
        }
        segments = segmentList
        placeholderIndices = placeholderIndexSet
    }

    override fun toString(parameterResolver: ParameterValueResolver): String {
        val sb = StringBuilder()
        for (ii in segments.indices) {
            val segment = if (placeholderIndices.contains(ii)) {
                val placeholderName = segments[ii]
                parameterResolver.resolve(placeholderName)
            } else {
                segments[ii]
            }
            sb.append(segment)
        }
        return sb.toString()
    }
}
