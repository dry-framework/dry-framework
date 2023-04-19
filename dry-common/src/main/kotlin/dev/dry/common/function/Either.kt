package dev.dry.common.function

import dev.dry.common.function.Either.Companion.left
import dev.dry.common.function.Either.Companion.right

sealed class Either<out L, out R> {
    /** * Represents the left side of [Either] class which by convention is a "Failure". */
    data class Left<out L>(val a: L): Either<L, Nothing>()

    /** * Represents the right side of [Either] class which by convention is a "Success". */
    data class Right<out R>(val b: R): Either<Nothing, R>()

    companion object {
        /**
         * Creates a Left type.
         * @see Left
         */
        @JvmStatic
        fun <L> left(a: L) = Left(a)


        /**
         * Creates a Left type.
         * @see Right
         */
        @JvmStatic
        fun <R> right(b: R) = Right(b)

        @JvmStatic
        @JvmName(name = "tryCatch")
        inline fun <R> catch(f: () -> R): Either<Throwable, R> {
            return try {
                right(f())
            } catch (th: Throwable) {
                left(th)
            }
        }
    }

    /**
     * Returns true if this is a Right, false otherwise.
     * @see Right
     */
    val isRight get() = this is Right<R>

    /**
     * Returns true if this is a Left, false otherwise.
     * @see Left
     */
    val isLeft get() = this is Left<L>

    /**
     * Applies fnL if this is a Left or fnR if this is a Right.
     * @see Left
     * @see Right
     */
    fun <T> fold(fnL: (L) -> T, fnR: (R) -> T): T =
        when (this) {
            is Left -> fnL(a)
            is Right -> fnR(b)
        }
}

/**
 * Composes 2 functions
 * See <a href="https://proandroiddev.com/kotlins-nothing-type-946de7d464fb">Credits to Alex Hart.</a>
 */
fun <A, B, C> ((A) -> B).c(f: (B) -> C): (A) -> C = {
    f(this(it))
}

/**
 * Right-biased flatMap() FP convention which means that Right is assumed to be the default case
 * to operate on. If it is Left, operations like map, flatMap, ... return the Left value unchanged.
 */
fun <T, L, R> Either<L, R>.flatMap(fn: (R) -> Either<L, T>): Either<L, T> =
    when (this) {
        is Either.Left -> Either.Left(a)
        is Either.Right -> fn(b)
    }

/**
 * Right-biased map() FP convention which means that Right is assumed to be the default case
 * to operate on. If it is Left, operations like map, flatMap, ... return the Left value unchanged.
 */
fun <T, L, R> Either<L, R>.map(fn: (R) -> (T)): Either<L, T> = this.flatMap(fn.c(::right))

/**
 * Map, or transform, the left value [L] of this [Either] to a new value [T].
 *
 * ```kotlin
 * fun test() {
 *  Either.right(12).mapLeft { _: Nothing -> "flower" } shouldBe Either.Right(12)
 *  Either.left(12).mapLeft { _: Int -> "flower" }  shouldBe Either.Left("flower")
 * }
 * ```
 */
fun <T, L, R> Either<L, R>.mapLeft(fn: (L) -> (T)): Either<T, R> =
    when (this) {
        is Either.Left -> left(fn(a))
        is Either.Right -> this
    }

fun <L, R> Either<L, Either<L, R>>.flatten(): Either<L, R> =
    flatMap { it }

/** Returns the value from this `Right` or the given argument if this is a `Left`.
 *  Right(12).getOrElse(17) RETURNS 12 and Left(12).getOrElse(17) RETURNS 17
 */
fun <L, R> Either<L, R>.orElse(value: R): R =
    when (this) {
        is Either.Left -> value
        is Either.Right -> b
    }

/** Returns the value from this `Right` or null if this is a `Left`.
 *  Right(12).orNull() RETURNS 12 and Left(12).orNull() RETURNS null
 */
fun <L, R> Either<L, R>.orNull(): R? =
    when (this) {
        is Either.Left -> null
        is Either.Right -> b
    }

/**
 * Left-biased onFailure() FP convention dictates that when this class is Left, it'll perform
 * the onFailure functionality passed as a parameter, but, overall will still return an either
 * object so you chain calls.
 */
fun <L, R> Either<L, R>.onFailure(fn: (failure: L) -> Unit): Either<L, R> =
    this.apply { if (this is Either.Left) fn(a) }

/**
 * Right-biased onSuccess() FP convention dictates that when this class is Right, it'll perform
 * the onSuccess functionality passed as a parameter, but, overall will still return an either
 * object so you chain calls.
 */
fun <L, R> Either<L, R>.onSuccess(fn: (success: R) -> Unit): Either<L, R> =
    this.apply { if (this is Either.Right) fn(b) }
