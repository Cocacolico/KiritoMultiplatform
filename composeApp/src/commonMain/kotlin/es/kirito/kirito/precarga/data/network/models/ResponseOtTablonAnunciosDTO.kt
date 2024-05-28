package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseOtTablonAnunciosDTO(
    @SerialName("idAnuncio") val id: String,
    val idUsuario: String,
    val fecha: String,
    val titulo: String,
    val explicacion: String,
    val etiqueta1: String?,
    val etiqueta2: String?,
    val etiqueta3: String?,
    val updated: String?
)
