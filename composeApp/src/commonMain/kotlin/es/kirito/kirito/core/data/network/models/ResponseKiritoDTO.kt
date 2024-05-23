package es.kirito.kirito.core.data.network.models

import kotlinx.serialization.Serializable


/**
 * La respuesta genérica que Jesús nos manda desde la API.
 * */
@Serializable
data class ResponseKiritoDTO<T>(
    val error: ResponseKiritoErrorDTO,
    val respuesta: T?
)

