package es.kirito.kirito.core.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestComplementosGraficoDTO (
    val peticion: String,
    val id_grafico: List<String>
)
