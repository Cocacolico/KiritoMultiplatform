package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseNotasTrenDTO(
    @SerialName("id_nota_al_tren") var id: String,
    @SerialName("id_grafico") var idGrafico: String,
    @SerialName("tren") var tren: String,
    @SerialName("lunes") var lunes: String = "",
    @SerialName("martes") var martes: String = "",
    @SerialName("miercoles") var miercoles: String = "",
    @SerialName("jueves") var jueves: String = "",
    @SerialName("viernes") var viernes: String = "",
    @SerialName("sabado") var sabado: String = "",
    @SerialName("domingo") var domingo: String = "",
    @SerialName("festivo") var festivo: String = "",
    @SerialName("notaAlTren") var nota: String = "",
)
