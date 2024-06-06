package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseEquivalenciasDTO(
    @SerialName("id_grafico") var idGrafico: String,
    @SerialName("turno") var turno: String,
    @SerialName("equivalencia") var equivalencia: String,
)
