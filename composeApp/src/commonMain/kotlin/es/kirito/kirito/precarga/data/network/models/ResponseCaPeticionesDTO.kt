package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName

data class ResponseCaPeticionesDTO(
    @SerialName("id_peticion_cambio") var id: String? = null,
    @SerialName("id_usuario_pide") var idUsuarioPide: String? = null,
    @SerialName("turno_usuario_pide") var turnoUsuarioPide: String? = null,
    @SerialName("id_usuario_recibe") var idUsuarioRecibe: String? = null,
    @SerialName("turno_usuario_recibe") var turnoUsuarioRecibe: String? = null,
    @SerialName("fecha_cambio") var fecha: String? = null,
    @SerialName("dt_peticion") var dtPeticion: String? = null,
    @SerialName("dt_respuesta") var dtRespuesta: String? = null,
    @SerialName("estado") var estado: String? = null,
)
