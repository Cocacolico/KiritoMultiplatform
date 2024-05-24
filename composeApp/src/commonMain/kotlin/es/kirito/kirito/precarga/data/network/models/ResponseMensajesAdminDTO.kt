package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName

data class ResponseMensajesAdminDTO(
    @SerialName("id_mensaje") var id: String? = null,
    @SerialName("titulo") var titulo: String? = null,
    @SerialName("mensaje") var mensaje: String? = null,
    @SerialName("enviado") var enviado: String? = null,
    @SerialName("enviado_por") var enviadoPor: String? = null,
    @SerialName("estado") var estado: String? = null,
)