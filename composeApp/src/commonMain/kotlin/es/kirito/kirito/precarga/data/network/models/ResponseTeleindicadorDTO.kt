package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseTeleindicadorDTO(
    @SerialName("id_relacion") var id: String,
    @SerialName("tren") var tren: String = "00000",
    @SerialName("lunes") var lunes: String = "",
    @SerialName("martes") var martes: String = "",
    @SerialName("miercoles") var miercoles: String = "",
    @SerialName("jueves") var jueves: String = "",
    @SerialName("viernes") var viernes: String = "",
    @SerialName("sabado") var sabado: String = "",
    @SerialName("domingo") var domingo: String = "",
    @SerialName("festivo") var festivo: String = "",
    @SerialName("notas") var notas: String = "",
    @SerialName("codigo") var codigo: String = "",
    @SerialName("vehiculo") var vehiculo: String = "",
)