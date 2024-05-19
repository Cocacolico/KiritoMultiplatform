package es.kirito.kirito.login.data.network

import es.kirito.kirito.core.data.network.ResponseKiritoErrorDTO
import kotlinx.serialization.Serializable

@Serializable
data class ResponseOtEstacionesDTO (
    val respuesta: List<ResponseRespuestaOtEstaciones>?
)
@Serializable
data class ResponseRespuestaOtEstaciones(
    @Serializable var nombre: String = "",
    @Serializable var acronimo: String = "",
    @Serializable var numero: String = "",
)