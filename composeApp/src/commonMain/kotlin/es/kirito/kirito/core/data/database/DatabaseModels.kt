package es.kirito.kirito.core.data.database

import androidx.room.Dao
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface KiritoDao {
//////////////////////GR_listado_graficos (Yo lo llamaré GrGraficos)
@Upsert()
suspend fun upsertGrafico(graficos: GrGraficos)

@Query("Select * from tabla_gr_graficos order by fechaInicio DESC")
suspend fun getGraficos(): List<GrGraficos>

@Query("Select * from tabla_gr_graficos order by fechaInicio ASC")
suspend fun getGraficosRaw(): List<GrGraficos>

@Query("delete from tabla_gr_graficos")
suspend fun deleteAllGraficos()

@Query(
    "Select * from tabla_gr_graficos " +
            "where tabla_gr_graficos.fechaInicio <= :fechaElegida  " +
            "AND tabla_gr_graficos.fechaFinal >= :fechaElegida "
)
suspend fun getGraficoDeUnDia(fechaElegida: Long?): GrGraficos?

@Query(
    "Select * from tabla_gr_graficos " +
            "where id_grafico = :id " +
            "limit 1"
)
suspend fun getGraficoDeUnId(id: Long?): GrGraficos

@Query(
    "Select * from tabla_gr_graficos " +
            "where fechaInicio between :fechaElegida AND (:fechaElegida + 14) " +
            "OR fechaFinal between :fechaElegida AND (:fechaElegida + 14) " +
            "OR fechaInicio <= :fechaElegida AND fechaFinal >= :fechaElegida " +
            "except " +//Excepto el gráfico de hoy, si estuviese también.
            " Select * from tabla_gr_graficos " +
            "where tabla_gr_graficos.fechaInicio <= :fechaElegida  " +
            "AND tabla_gr_graficos.fechaFinal >= :fechaElegida " +
            "order by fechaInicio asc"
)
suspend fun getGraficosDeEnCatorceDias(fechaElegida: Long?): List<GrGraficos>

@Query(
    "Select * from tabla_gr_graficos " +
            "where fechaInicio between :fechaElegida AND (:fechaElegida + 180) " +
            "OR fechaFinal between :fechaElegida AND (:fechaElegida + 180) " +
            "OR fechaInicio <= :fechaElegida AND fechaFinal >= :fechaElegida"
)
suspend fun getGraficosDeSeisMeses(fechaElegida: Long?): List<GrGraficos>
}