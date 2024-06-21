package es.kirito.kirito.menu.data.network.models

data class RequestChangePassword(
    val peticion: String,
    val old_password: String,
    val new_password: String
)