package kirito.login.domain

import es.kirito.kirito.login.data.network.ResidenciaDTO

data class RegisterState(
    val residencias: List<ResidenciaDTO> = emptyList(),
    val usuario: String? = ""
)