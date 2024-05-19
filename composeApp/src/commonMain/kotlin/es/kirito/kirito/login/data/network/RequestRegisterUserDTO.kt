package es.kirito.kirito.login.data.network

import kotlinx.serialization.Serializable

@Serializable
data class RequestRegisterUserDTO (
    val peticion: String,
    val username: String,
    val email: String,
    val name: String,
    val surname: String,
    val work_phone_ext: String,
    val work_phone: String,
    val personal_phone: String,
    val mostrar_telf_trabajo: String,
    val mostrar_telf_personal: String,
    val comentariosAlAdmin: String,
    val password: String,
)