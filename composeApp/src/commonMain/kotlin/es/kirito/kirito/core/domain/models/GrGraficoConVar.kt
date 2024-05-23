package es.kirito.kirito.core.domain.models


data class GrGraficosConVar(
    var idGrafico: Long,
    var fechaInicio: Long?,
    var fechaFinal: Long?,
    var descripcion: String?,
    var ficheroGrafico: String?,
    var ficheroAnexo: String?,
    var enlaceGraficoGdrive: String?,
    var nombreGraficoGdrive: String?,
    var enlaceAnexoGdrive: String?,
    var nombreAnexoGdrive: String?,
    var fechaUltimoCambio: Long?,
    var descargado: Boolean?
)