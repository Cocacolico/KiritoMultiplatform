package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseNotasTurnoDTO(
    @SerialName("id_nota_al_turno") var id: String,
    @SerialName("id_grafico") var idGrafico: String,
    @SerialName("turno") var turno: String,
    @SerialName("lunes") var lunes: String = "",
    @SerialName("martes") var martes: String = "",
    @SerialName("miercoles") var miercoles: String = "",
    @SerialName("jueves") var jueves: String = "",
    @SerialName("viernes") var viernes: String = "",
    @SerialName("sabado") var sabado: String = "",
    @SerialName("domingo") var domingo: String = "",
    @SerialName("festivo") var festivo: String = "",
    @SerialName("notaAlTurno") var nota: String = "",
)
