package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseColoresTrenesDTO(
    @SerialName("filtro") var filtro: String,
    @SerialName("color") var color: String = "",
)
