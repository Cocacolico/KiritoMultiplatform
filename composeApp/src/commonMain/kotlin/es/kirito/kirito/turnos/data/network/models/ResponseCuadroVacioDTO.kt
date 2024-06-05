package es.kirito.kirito.turnos.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseCuadroVacioDTO (
    @SerialName("id_detalle") var idDetalle: String,
    @SerialName("fecha") var fecha: String,
    @SerialName("turno") var turno: String? = null,
    @SerialName("tipo") var tipo: String,
    @SerialName("updated") var updated: String? = null,
)