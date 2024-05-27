package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseLocalizadoresDTO(
    @SerialName("fecha") var fecha: String? = null,
    @SerialName("turno") var turno: String? = null,
    @SerialName("localizador") var localizador: String? = null,
)
