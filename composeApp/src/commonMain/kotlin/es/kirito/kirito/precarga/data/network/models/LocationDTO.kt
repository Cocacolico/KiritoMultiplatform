package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDTO(
    @SerialName("lat") var lat: Double? = null,
    @SerialName("lng") var lng: Double? = null
)
