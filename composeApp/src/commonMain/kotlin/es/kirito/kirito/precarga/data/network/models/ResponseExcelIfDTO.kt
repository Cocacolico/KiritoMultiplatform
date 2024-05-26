package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseExcelIfDTO(
    @SerialName("id_detalle_grafico") var idDetalleGrafico: String,
    @SerialName("id_grafico") var idGrafico: String,
    @SerialName("numero_turno") var numeroTurno: String,
    @SerialName("orden_tarea") var ordenTarea: String,
    @SerialName("lunes") var lunes: String = "",
    @SerialName("martes") var martes: String = "",
    @SerialName("miercoles") var miercoles: String = "",
    @SerialName("jueves") var jueves: String = "",
    @SerialName("viernes") var viernes: String = "",
    @SerialName("sabado") var sabado: String = "",
    @SerialName("domingo") var domingo: String = "",
    @SerialName("festivo") var festivo: String = "",
    @SerialName("dia_semana") var diaSemana: String = "",
    @SerialName("comentario_al_turno") var comentarioAlTurno: String? = null,
    @SerialName("turno_real") var turnoReal: String? = null,
    @SerialName("sitio_origen") var sitioOrigen: String? = null,
    @SerialName("hora_origen") var horaOrigen: String? = null,
    @SerialName("sitio_fin") var sitioFin: String? = null,
    @SerialName("hora_fin") var horaFin: String? = null
)
