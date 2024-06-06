package es.kirito.kirito.core.domain.models

import es.kirito.kirito.core.domain.util.isNotNullNorBlank

data class TablonAnunciosItem (
    val id: Long,
    val miId: Long,
    val miTurno: String?,
    val miTipo: String?,
    val idUsuario: Long,
    val nombreUsuario: String,
    val turnoUsuario: String?,
    val fecha: Long,
    val titulo: String,
    val explicacion: String,
    val etiqueta1: String?,
    val etiqueta2: String?,
    val etiqueta3: String?,
    val updated: Long?,

    ){
    /**
     * Devuelve una String con las etiquetas unidas con comas.
     * */
    fun etiquetasString(): String{
        var salida = if (etiqueta1.isNotNullNorBlank()) etiqueta1 else ""
        salida += if (etiqueta2.isNotNullNorBlank()) ",$etiqueta2" else ""
        salida += if (etiqueta3.isNotNullNorBlank()) ",$etiqueta3" else ""
        return salida ?: ""
    }

}