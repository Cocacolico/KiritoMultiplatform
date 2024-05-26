package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseExcesosGraficoDTO(
    @SerialName("fecha") var fecha: String? = null,
    @SerialName("mayor_dedicacion") var exceso: String? = null,
)