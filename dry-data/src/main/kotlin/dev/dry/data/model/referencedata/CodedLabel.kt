package dev.dry.data.model.referencedata

interface CodedLabel<T: CodedLabel<T, C>, C> : Comparable<T> {
    val code: C
    val label: String

    override operator fun compareTo(other: T): Int = label.compareTo(other.label)
}
