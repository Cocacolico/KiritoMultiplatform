package es.kirito.kirito.menu.domain

import es.kirito.kirito.core.data.database.LsUsers

data class ProfileState (
    val userID: Long = 0L,
    val residencia: String = "",
    val userData: LsUsers = LsUsers(),
    val showModificarDatosDialog: Boolean = false,
    val showCambiarPasswordDialog: Boolean = false
)


