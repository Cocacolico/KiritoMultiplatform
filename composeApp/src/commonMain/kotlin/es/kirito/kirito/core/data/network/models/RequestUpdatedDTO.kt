package es.kirito.kirito.core.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestUpdatedDTO(
    val peticion: String,
    val updated: String? = null
)