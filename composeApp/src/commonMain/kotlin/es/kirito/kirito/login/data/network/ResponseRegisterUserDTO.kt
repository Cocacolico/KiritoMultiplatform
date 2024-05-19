package es.kirito.kirito.login.data.network

import es.kirito.kirito.core.data.network.ResponseKiritoErrorDTO
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRegisterUserDTO (
    val error: ResponseKiritoErrorDTO,
    val respuesta: ResponseRespuestaRegisterUser
)
@Serializable
data class ResponseRespuestaRegisterUser(
    val RegistroSatisfactorio: Boolean,
    val PuedeAccederInmediatamente: Boolean,
)