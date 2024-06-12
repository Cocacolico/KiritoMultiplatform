package es.kirito.kirito.core.domain.util

fun Int?.toIntIfNull(valueIfNull: Int): Int = this ?: valueIfNull

fun media(x: Long, y: Long): Float = (x.toFloat() + y.toFloat()) / 2

fun Long.restar60(): Long = this % 60

fun Int.restar60(): Int = this % 60

fun Int.isEven(): Boolean = this.rem(2) == 0