package es.kirito.kirito.login.data.network

import kotlinx.serialization.Serializable


@Serializable
data class ResponseLoginDTO(
    val login: ResponseRespuestaLoginDTO,
    val seconds: Int? = null,
)
@Serializable
data class ResponseRespuestaLoginDTO(
    val id_usuario: String,
    val token: String,
)