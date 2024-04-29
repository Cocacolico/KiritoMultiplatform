package kirito.login.domain

import es.kirito.kirito.login.data.network.ResidenciaDTO

data class RegisterState(
    val residencias: List<ResidenciaDTO> = emptyList(),
    val residenciaSeleccionada: String? = "",
    val expanded: Boolean = false,
    val errorUsuarioErroneo: Boolean = false,
    val errorPasswordErroneo: Boolean = false,
    val usuario: String = "",
    val password: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val telefonoIntCorto:  String = "",
    val telefonoIntLargo:  String = "",
    val visibilidadTelefonoEmpresa: Boolean = false,
    val telefonoPersonal:  String = "",
    val visibilidadTelefonoPersonal: Boolean = false,
    val email: String = "",
    val passwordCheck: String = "",
    val errorPasswordNoCoincide: Boolean = false,
    val comentarios: String = ""
)