package es.kirito.kirito.core.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestIncluidosDTO(
    val peticion: String,
    val incluidos: String,
)
