package es.kirito.kirito.core.domain.util

fun Int?.toIntIfNull(valueIfNull: Int): Int {
    return this ?: valueIfNull
}

fun media(x: Long, y: Long): Float {
    return (x.toFloat() + y.toFloat()) / 2
}

fun Long.restar60(): Long {
    return this % 60
}
fun Int.restar60(): Int {
    return this % 60
}