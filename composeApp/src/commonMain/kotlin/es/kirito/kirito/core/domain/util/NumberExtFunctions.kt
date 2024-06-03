package es.kirito.kirito.core.domain.util

fun Int?.toIntIfNull(valueIfNull: Int): Int {
    return this ?: valueIfNull
}