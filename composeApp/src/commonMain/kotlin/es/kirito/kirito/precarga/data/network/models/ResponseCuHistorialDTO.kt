package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName

data class ResponseCuHistorialDTO (
    @SerialName("id_historial") var id: String,
    @SerialName("id_detalle") var idDetalle: String,
    @SerialName("turno") var turno: String,
    @SerialName("tipo") var tipo: String,
    @SerialName("nombre_debe") var nombreDebe: String?,
    @SerialName("actualizado") var updated: String?
)