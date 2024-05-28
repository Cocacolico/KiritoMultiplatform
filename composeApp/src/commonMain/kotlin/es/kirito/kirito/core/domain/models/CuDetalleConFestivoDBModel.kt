package es.kirito.kirito.core.domain.models

import es.kirito.kirito.core.domain.util.inicial
import es.kirito.kirito.core.domain.util.toLocalDate

data class CuDetalleConFestivoDBModel(
    var idDetalle: Long,
    var fecha: Long?,
    var tipo: String,
    var turno: String,
    var nombreDebe: String,
    var notas: String,
    var idFestivo: Long?,
    var descripcion: String?,
    var libra: Int?,
    var comj: Int?,
    var mermas: Int?,
    var excesos: Int?,
) {
    fun getDiaSemana(): String? {
        return if (fecha != null) {
            if (idFestivo == null)
                fecha.toLocalDate().dayOfWeek.inicial()
            else
                "F"
        } else null
    }
}