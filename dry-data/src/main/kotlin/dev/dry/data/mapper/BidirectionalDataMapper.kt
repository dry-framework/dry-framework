package dev.dry.data.mapper

open class BidirectionalDataMapper<S, T>(
    private val from: (S) -> T,
    private val to: (T) -> S,
) {
    fun mapFrom(value: S): T = from(value)
    fun mapTo(value: T): S = to(value)
}
