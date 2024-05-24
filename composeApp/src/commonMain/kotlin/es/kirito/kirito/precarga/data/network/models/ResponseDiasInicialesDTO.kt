package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName

data class ResponseDiasInicialesDTO(
    val anio: String,
    @SerialName("valor_inicial") val valorInicial: String,
    val tipo: String,
)
