package es.kirito.kirito.core.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestAnioDTO(
    val peticion: String,
    val anio: String? = null
)

