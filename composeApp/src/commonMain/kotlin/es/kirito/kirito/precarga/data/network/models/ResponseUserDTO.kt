package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseUserDTO(
    @SerialName("id_usuario") var id: String?,
    @SerialName("username") var username: String? = null,
    @SerialName("email") var email: String? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("surname") var surname: String? = null,
    @SerialName("work_phone") var workPhoneExt: String? = null,
    @SerialName("work_phone_ext") var workPhone: String? = null,
    @SerialName("personal_phone") var personalPhone: String? = null,
    @SerialName("mostrar_telf_trabajo") var mostrarTelfTrabajo: String? = null,
    @SerialName("mostrar_telf_personal") var mostrarTelfPersonal: String? = null,
    @SerialName("photo") var photo: String? = null,
    @SerialName("created") var created: String? = null,
    @SerialName("last_login") var lastLogin: String? = null,
    @SerialName("disabled") var disabled: String? = null,
    @SerialName("admin") var admin: String? = null,
    @SerialName("key_ics") var keyIcs: String? = null,
    @SerialName("key_access_web") var keyAccessWeb: String? = null,
    @SerialName("comentariosAlAdmin") var comentariosAlAdmin: String? = null,
    @SerialName("cambios_activados") var cambiosActivados: String? = null,
    @SerialName("cambios_activados_cuando") var cambiosActivadosCuando: String? = null,
    @SerialName("recibir_email_notificaciones") var recibirEmailNotificaciones: String? = null,
    @SerialName("mostrar_cuadros") var mostrarCuadros: String? = null,
    @SerialName("mostrar_cuadros_cuando") var mostrarCuadrosCuando: String? = null,
    @SerialName("notas") var notas: String? = null,
    @SerialName("peticiones_diarias") var peticionesDiarias: String? = null
)