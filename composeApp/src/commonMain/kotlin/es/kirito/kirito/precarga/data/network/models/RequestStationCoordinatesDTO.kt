package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestStationCoordinatesDTO(
    val peticion: String,
    val busqueda: String
)
