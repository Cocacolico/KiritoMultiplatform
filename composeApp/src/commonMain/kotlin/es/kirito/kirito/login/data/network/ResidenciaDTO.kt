package es.kirito.kirito.login.data.network

import kotlinx.serialization.Serializable

@Serializable
data class ResidenciaDTO(
    val directorio: String,
    val nombre: String,
)
