package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName

data class ResponseExcesosGraficoDTO(
    @SerialName("fecha") var fecha: String? = null,
    @SerialName("mayor_dedicacion") var exceso: String? = null,
)