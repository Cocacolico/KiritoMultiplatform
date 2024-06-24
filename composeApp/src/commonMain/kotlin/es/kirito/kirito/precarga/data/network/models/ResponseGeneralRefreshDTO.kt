package es.kirito.kirito.precarga.data.network.models

import es.kirito.kirito.core.data.network.models.ResponseCuDetallesDTO
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGeneralRefreshDTO(
    val festivos: List<ResponseOtFestivosDTO>?,
    val graficos: List<ResponseGrGraficosDTO>?,
    val turnos: List<ResponseCuDetallesDTO>?,
    val historiales: List<ResponseCuHistorialDTO>?,
    val mensajes: List<ResponseMensajesAdminDTO>?,
    val cambios: List<ResponseCaPeticionesDTO>?,
    val telefonos_importantes: List<ResponseTelefonoEmpresaDTO>?,
    val tablones: List<ResponseOtTablonAnunciosDTO>?,
    val usuarios: List<ResponseUserDTO>?,
    val actualizados: List<ResponseDelAndUpdElementsDTO>?,
    val borrados: List<ResponseDelAndUpdElementsDTO>?,
)
