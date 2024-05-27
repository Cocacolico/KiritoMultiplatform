package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseStationCoordinatesDTO(
    @SerialName("results") var results: ArrayList<ResultsDTO> = arrayListOf(),
    @SerialName("status") var status: String? = null
)
