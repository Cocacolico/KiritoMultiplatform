package es.kirito.kirito.core.data.network

import kotlinx.serialization.Serializable

@Serializable
data class ResponseKiritoErrorDTO(
    val errorCode: String,
    val errorDesc: String
)
