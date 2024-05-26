package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseTelefonoEmpresaDTO(
    val id: String,
    val empresa: String,
    @SerialName("tipo_servicio") val tipoServicio: String,
    val nombre: String,
    val telefono1: String,
    val telefono2: String
)
