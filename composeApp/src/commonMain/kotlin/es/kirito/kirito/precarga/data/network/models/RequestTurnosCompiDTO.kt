package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestTurnosCompiDTO (
    val peticion: String,
    val anio: String,
    val id_compi: String
)