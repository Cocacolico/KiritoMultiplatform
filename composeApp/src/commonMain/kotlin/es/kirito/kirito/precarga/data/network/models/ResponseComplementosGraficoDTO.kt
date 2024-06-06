package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseComplementosGraficoDTO(
    @SerialName("id_grafico") val idGrafico: String,
    @SerialName("excelif") val excelIF: List<ResponseExcelIfDTO>,
    @SerialName("excellibreta") val grTareas: List<ResponseGrTareasDTO>,
    @SerialName("notastren") val notasTren: List<ResponseNotasTrenDTO>,
    @SerialName("notasturnos") val notasTurnos: List<ResponseNotasTurnoDTO>,
    @SerialName("equivalencias") val equivalencias: List<ResponseEquivalenciasDTO>,
)