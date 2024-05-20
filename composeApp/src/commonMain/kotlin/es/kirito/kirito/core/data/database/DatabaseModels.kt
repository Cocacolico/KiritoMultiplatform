package es.kirito.kirito.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface KiritoDao {
    //////////////////////GR_listado_graficos (Yo lo llamaré GrGraficos)
    @Upsert()
    suspend fun upsertGrafico(graficos: GrGraficos)

    @Query("Select * from tabla_gr_graficos order by fechaInicio DESC")
    fun getGraficos(): Flow<List<GrGraficos>>

    @Query("Select * from tabla_gr_graficos order by fechaInicio ASC")
    fun getGraficosRaw(): Flow<List<GrGraficos>>

    @Query("delete from tabla_gr_graficos")
    suspend fun deleteAllGraficos()

    @Query(
        "Select * from tabla_gr_graficos " +
                "where tabla_gr_graficos.fechaInicio <= :fechaElegida  " +
                "AND tabla_gr_graficos.fechaFinal >= :fechaElegida "
    )
    fun getGraficoDeUnDia(fechaElegida: Long?): Flow<GrGraficos?>

    @Query(
        "Select * from tabla_gr_graficos " +
                "where id_grafico = :id " +
                "limit 1"
    )
    fun getGraficoDeUnId(id: Long?): Flow<GrGraficos?>

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
    fun getGraficosDeEnCatorceDias(fechaElegida: Long?): Flow<List<GrGraficos>>

    @Query(
        "Select * from tabla_gr_graficos " +
                "where fechaInicio between :fechaElegida AND (:fechaElegida + 180) " +
                "OR fechaFinal between :fechaElegida AND (:fechaElegida + 180) " +
                "OR fechaInicio <= :fechaElegida AND fechaFinal >= :fechaElegida"
    )
    fun getGraficosDeSeisMeses(fechaElegida: Long?): Flow<List<GrGraficos>>

    ///////////////////// OT_Estaciones
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstacion(estaciones: Estaciones)
    @Upsert()
    suspend fun upsertEstacion(estacionWCoordinates: Estaciones)

    @Query("select exists (select * from tabla_estaciones)")
    fun hayEstaciones(): Flow<Boolean>

    @Query("Select * from tabla_estaciones where nombre in " +
            "(Select distinct * from (" +
            "select distinct sitio_origen from tabla_gr_tareas " +
            "union " +
            "select distinct sitio_fin from tabla_gr_tareas) " +
            "as combined_column)")
    fun getEstacionesDeGraficos(): Flow<List<Estaciones>>

    @Query("Select distinct * from (" +
            "select distinct sitio_origen from tabla_gr_tareas " +
            "union " +
            "select distinct sitio_fin from tabla_gr_tareas) " +
            "as combined_column")
    fun getEstacionesEnGraficos(): Flow<List<String>>

    @Query("Select exists (select * from tabla_estaciones where nombre = :nombre)")
    fun isStationInEstaciones(nombre: String): Flow<Boolean>



    @Query("Select * from tabla_estaciones")
    fun getAllEstaciones(): Flow<List<Estaciones>>
    @Query("update tabla_estaciones " +
            "set longitud = :longitud, latitud = :lat " +
            "where nombre = :nombre"
    )
    fun setStationCoordinates(longitud: Float, lat: Float, nombre: String)

    @Query("Select * from tabla_estaciones where esDelGrafico and latitud is null")
    fun getChartStationsWithoutCoordinates(): Flow<List<Estaciones>>
}

