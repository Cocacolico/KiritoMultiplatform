package es.kirito.kirito.login.data.network

import kotlinx.serialization.Serializable

@Serializable
data class ResponseResidenciasDTO(
    val residencias: List<ResidenciaDTO>
)
