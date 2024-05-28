package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDiasInicialesDTO(
    val anio: String,
    @SerialName("valor_inicial") val valorInicial: String,
    val tipo: String,
)
