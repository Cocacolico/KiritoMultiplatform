package es.kirito.kirito.menu.domain

data class ProfileState (
    val id: Long = -1L,
    val username: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val workPhone: String = "",
    val workPhoneExt: String = "",
    val personalPhone: String = "",
    val showModificarDatosDialog: Boolean = false,
    val showCambiarPasswordDialog: Boolean = false,
    val oldPassword: String = "",
    val newPassword: String = "",
    val checkNewPassword: String = "",
    val passwordTiene5Caracteres: Boolean = false,
    val passwordTieneMayuscula: Boolean = false,
    val passwordTieneNumero: Boolean = false,
    val passwordsCoinciden: Boolean = false
)


