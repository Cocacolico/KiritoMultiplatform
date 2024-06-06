package es.kirito.kirito.core.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestSimpleDTO(
    val peticion: String
)
