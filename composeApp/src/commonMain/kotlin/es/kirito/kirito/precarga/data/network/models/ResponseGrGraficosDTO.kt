package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGrGraficosDTO (
    @SerialName("id_grafico") var idGrafico: String,
    @SerialName("fechaInicio") var fechaInicio: String? = null,
    @SerialName("fechaFinal") var fechaFinal: String? = null,
    @SerialName("descripcion") var descripcion: String? = null,
    @SerialName("nombreFicheroGrafico") var nombreFicheroGrafico: String? = null,
    @SerialName("nombreFicheroAnexo") var nombreFicheroAnexo: String? = null,
    @SerialName("enlaceGDriveFicheroGrafico") var enlaceGDriveFicheroGrafico: String? = null,
    @SerialName("nombreGDriveFicheroGrafico") var nombreGDriveFicheroGrafico: String? = null,
    @SerialName("enlaceGDriveFicheroAnexo") var enlaceGDriveFicheroAnexo: String? = null,
    @SerialName("nombreGDriveFicheroAnexo") var nombreGDriveFicheroAnexo: String? = null,
    @SerialName("fechaUltimoCambio") var fechaUltimoCambio: String? = null
)