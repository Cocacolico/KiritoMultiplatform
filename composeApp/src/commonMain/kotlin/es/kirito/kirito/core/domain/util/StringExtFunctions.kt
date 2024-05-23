package es.kirito.kirito.core.domain.util

fun String?.isNotNullNorEmpty(): Boolean {
    return !this.isNullOrEmpty()
}

fun String?.isNotNullNorBlank(): Boolean {
    return !this.isNullOrBlank()
}