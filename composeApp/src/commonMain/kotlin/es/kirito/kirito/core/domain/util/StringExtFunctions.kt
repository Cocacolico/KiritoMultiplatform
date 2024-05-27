package es.kirito.kirito.core.domain.util

fun String?.isNotNullNorEmpty(): Boolean {
    return !this.isNullOrEmpty()
}

fun String?.isNotNullNorBlank(): Boolean {
    return !this.isNullOrBlank()
}

fun Any?.toStringIfNull(texto: String): String {
    return this?.toString() ?: texto
}

/** Si la cadena vale "1" -> true, en cualquier otro caso -> false. **/
fun String.toMyBoolean(): Boolean {
    return this == "1"
}

fun String.normalizeAndRemoveAccents(): String {
//    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
//    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
//    return pattern.matcher(normalized).replaceAll("")
    //TODO: Esto no funcionará bien.
    return this.replace(Regex("[^\\x00-\\x7F]"), "")
}