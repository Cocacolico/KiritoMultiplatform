package es.kirito.kirito.login.data.network

import kotlinx.serialization.Serializable


@Serializable
data class ResponseLogin(
    val login: ResponseRespuestaLogin,
    val seconds: Int? = null,
)
@Serializable
data class ResponseRespuestaLogin(
    val idUsuario: String,
    val token: String,
)