package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseCuDetallesDTO (
    @SerialName("id_detalle") var idDetalle: String,
    @SerialName("id_usuario") var idUsuario: String,
    @SerialName("fecha") var fecha: String,
    @SerialName("dia_semana") var diaSemana: String? = null,
    @SerialName("turno") var turno: String? = null,
    @SerialName("tipo") var tipo: String,
    @SerialName("notas") var notas: String,
    @SerialName("nombre_debe") var nombreDebe: String,
    @SerialName("updated") var updated: String? = null,
    @SerialName("LIBRa") var libra: String? = null,
    @SerialName("COMJ") var comj: String? = null,
    @SerialName("mermas") var mermas: String? = null,
    @SerialName("excesos") var excesos: String? = null,
)