package es.kirito.kirito.login.data.network

import kotlinx.serialization.Serializable

@Serializable
data class RequestRegisterUserDTO (
    val peticion: String,
    val username: String,
    val email: String,
    val name: String,
    val surname: String,
    val workPhoneExt: String,
    val workPhone: String,
    val personalPhone: String,
    val mostrarTelfTrabajo: String,
    val mostrarTelfPersonal: String,
    val comentariosAlAdmin: String,
    val password: String,
)