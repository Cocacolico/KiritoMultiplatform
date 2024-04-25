package es.kirito.kirito.core.data.network

import kotlinx.serialization.Serializable

@Serializable
data class RequestSimpleDTO(
    val peticion: String
)
