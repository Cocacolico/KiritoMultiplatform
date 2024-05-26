package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestGraficoDTO(
    val peticion: String,
    val id_grafico: String
)
