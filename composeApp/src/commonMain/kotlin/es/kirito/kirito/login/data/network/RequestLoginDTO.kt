package es.kirito.kirito.login.data.network

import kotlinx.serialization.Serializable

@Serializable
data class RequestLoginDTO(
    val peticion: String,
    val usuario: String,
    val password: String,
    val descripcion_dispositivo: String,
    val tokenFCM: String
)