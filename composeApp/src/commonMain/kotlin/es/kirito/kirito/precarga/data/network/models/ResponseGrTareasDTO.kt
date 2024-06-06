package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGrTareasDTO(
    @SerialName("id_detalle_libreta") var idDetalleLibreta: String,
    @SerialName("id_grafico") var idGrafico: String,
    @SerialName("turno") var turno: String,
    @SerialName("orden_servicio") var ordenServicio: String,
    @SerialName("servicio") var servicio: String,
    @SerialName("tipo_servicio") var tipoServicio: String? = null,
    @SerialName("dia_semana") var diaSemana: String = "",
    @SerialName("sitio_origen") var sitioOrigen: String? = null,
    @SerialName("hora_origen") var horaOrigen: String? = null,
    @SerialName("sitio_fin") var sitioFin: String? = null,
    @SerialName("hora_fin") var horaFin: String? = null,
    @SerialName("vehiculo") var vehiculo: String? = null,
    @SerialName("observaciones") var observaciones: String? = null,
    @SerialName("inserted") var inserted: String
)
