package es.kirito.kirito.menu.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestEditarMiUsuario(
    @SerialName("peticion") var peticion: String,
    @SerialName("id") var id: String,
    @SerialName("email") var email: String?,
    @SerialName("surname") var surname: String?,
    @SerialName("name") var name: String?,
    @SerialName("work_phone_ext") var work_phone: String?,
    @SerialName("work_phone") var work_phone_ext: String?,//El corto.
    @SerialName("personal_phone") var personal_phone: String?,
    @SerialName("mostrar_telf_trabajo") var mostrar_telf_trabajo: String?,
    @SerialName("mostrar_telf_personal") var mostrar_telf_personal: String?,
    @SerialName("cambios_activados") var cambios_activados: String?,
    @SerialName("recibir_email_notificaciones") var recibir_email_notificaciones: String?,
    @SerialName("mostrar_cuadros") var mostrar_cuadros: String?
)
