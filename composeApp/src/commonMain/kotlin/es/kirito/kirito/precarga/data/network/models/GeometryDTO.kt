package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeometryDTO(
    @SerialName("location") var location: LocationDTO? = LocationDTO(),
)