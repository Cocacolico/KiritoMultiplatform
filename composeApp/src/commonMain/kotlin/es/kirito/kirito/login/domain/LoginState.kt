package es.kirito.kirito.login.domain

import es.kirito.kirito.login.data.network.ResidenciaDTO

data class LoginState(
    val residencias: List<ResidenciaDTO> = emptyList(),
    val residenciaSeleccionada: String? = "",
    val urlResidenciaSeleccionada: String? = "",
    val expanded: Boolean = false,
    val usuario: String = "",
    val password: String = "",
    val modoDevActivado: Boolean = false,
    val errorCampoUserPassword: Boolean = false
)