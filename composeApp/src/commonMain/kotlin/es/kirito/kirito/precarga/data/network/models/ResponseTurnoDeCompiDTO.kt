package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseTurnoDeCompiDTO(
    @SerialName("id_detalle") var id_detalle: String,
    @SerialName("id_usuario") var id_usuario: String,
    @SerialName("fecha") var fecha: String,
    @SerialName("turno") var turno: String? = "-",
    @SerialName("tipo") var tipo: String = "-",
)
