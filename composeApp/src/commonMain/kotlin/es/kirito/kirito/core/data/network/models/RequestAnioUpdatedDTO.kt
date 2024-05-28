package es.kirito.kirito.core.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestAnioUpdatedDTO(
    val peticion: String,
    val anio: String,
    val updated: String? = null
)
