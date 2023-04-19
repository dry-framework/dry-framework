package dev.dry.data.model.referencedata

interface OrderedCodedLabel<T: OrderedCodedLabel<T, C>, C> : CodedLabel<T, C> {
    val ordinal: Int

    override operator fun compareTo(other: T): Int {
        val ordinalComparisonResult = ordinal.compareTo(other.ordinal)
        return if (ordinalComparisonResult == 0) {
            super.compareTo(other)
        } else {
            ordinalComparisonResult
        }
    }
}
