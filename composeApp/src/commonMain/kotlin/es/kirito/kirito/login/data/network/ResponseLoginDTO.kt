package es.kirito.kirito.login.data.network

import kotlinx.serialization.Serializable

@Serializable
data class ResponseLoginDTO(
    val respuesta: ResponseLogin? = null,
)
@Serializable
data class ResponseLogin(
    val login: ResponseRespuestaLogin,
    val seconds: Int? = null,
)
@Serializable
data class ResponseRespuestaLogin(
    val id_usuario: String,
    val token: String,
)