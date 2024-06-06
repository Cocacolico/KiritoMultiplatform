package es.kirito.kirito.login.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseOtEstacionesDTO(
    @SerialName("nombre") var nombre: String = "",
    @SerialName("acronimo") var acronimo: String = "",
    @SerialName("numero") var numero: String = "",
)