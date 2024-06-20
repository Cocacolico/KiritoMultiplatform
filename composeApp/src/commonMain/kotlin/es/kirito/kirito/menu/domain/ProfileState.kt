package es.kirito.kirito.menu.domain

data class ProfileState (
    val id: Long = -1L,
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val workPhone: String = "",
    val workPhoneExt: String = "",
    val personalPhone: String = "",
    val showModificarDatosDialog: Boolean = false,
    val showCambiarPasswordDialog: Boolean = false
)


