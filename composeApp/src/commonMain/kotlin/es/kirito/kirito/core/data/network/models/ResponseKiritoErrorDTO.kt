package es.kirito.kirito.core.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class ResponseKiritoErrorDTO(
    val errorCode: String,
    val errorDesc: String
)
