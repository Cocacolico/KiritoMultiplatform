package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDelAndUpdElementsDTO(
    val id: String,
    val tabla: String,
    @SerialName("id_elemento") val idElemento: String?,
    val included: String
)
