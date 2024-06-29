package es.kirito.kirito.core.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestEditarTurnoDTO(
    val peticion: String,
    val id_detalle: String?,
    val fecha: String,
    val turno: String,
    val tipo: String,
    val notas: String?,
    val nombre_debe: String?,
    val dias_ganados: List<DiasGanadosPair>,
)