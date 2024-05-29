package es.kirito.kirito.turnos.data.network.models

import kotlinx.serialization.Serializable

/** Request Subir cuadro vacío **/
@Serializable
data class RequestSubirCuadroVacioDTO(
    val peticion: String,
    val anio: String,
    val sobreescribir: String
)