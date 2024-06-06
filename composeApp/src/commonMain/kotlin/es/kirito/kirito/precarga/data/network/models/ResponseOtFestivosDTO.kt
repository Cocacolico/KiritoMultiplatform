package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseOtFestivosDTO(
   @SerialName("id_festivo")  val idFestivo: String? = null,
    val fecha: String? = null,
    val descripcion: String? = null
)
