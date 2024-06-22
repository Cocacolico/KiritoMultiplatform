package es.kirito.kirito.menu.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestChangePassword(
    val peticion: String,
    val old_password: String,
    val new_password: String
)