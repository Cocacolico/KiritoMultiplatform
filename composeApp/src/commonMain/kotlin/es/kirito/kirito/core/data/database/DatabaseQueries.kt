package es.kirito.kirito.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import es.kirito.kirito.core.domain.models.CaPeticionesDomain
import es.kirito.kirito.core.domain.models.CuDetalleCompisConCambios
import es.kirito.kirito.core.domain.models.CuDetalleConFestivoDBModel
import es.kirito.kirito.core.domain.models.EtiquetaDeTablonAnuncios
import es.kirito.kirito.core.domain.models.GrGraficosConVar
import es.kirito.kirito.core.domain.models.GrTareaConClima
import es.kirito.kirito.core.domain.models.GrTareaBuscador
import es.kirito.kirito.core.domain.models.TablonAnunciosItem
import es.kirito.kirito.core.domain.models.TablonAnunciosNavigationParams
import es.kirito.kirito.core.domain.models.TelefonosUsuario
import es.kirito.kirito.core.domain.models.TurnoBuscador
import es.kirito.kirito.core.domain.models.TurnoDeCompi
import es.kirito.kirito.core.domain.models.TurnoDeEquivalencia
import es.kirito.kirito.core.domain.models.TurnoPrxTr
import es.kirito.kirito.core.domain.models.TurnoYTipo
import es.kirito.kirito.core.domain.models.TurnosPorTipo
import es.kirito.kirito.core.domain.models.UserConfig
import kotlinx.coroutines.flow.Flow

@Dao
interface KiritoDao {

    @Query(
        "select tabla_cu_detalle.idDetalle, tabla_cu_detalle.idUsuario, tabla_cu_detalle.fecha, " +
                "tabla_cu_detalle.turno, tabla_cu_detalle.tipo, tabla_cu_detalle.notas, tabla_cu_detalle.nombreDebe," +
                "tabla_gr_excel_if.idGrafico, tabla_gr_excel_if.sitioOrigen, " +
                "tabla_gr_excel_if.horaOrigen, tabla_gr_excel_if.sitioFin, tabla_gr_excel_if.horaFin, " +
                "tabla_cu_detalle.diaSemana, tabla_cu_detalle.libra, tabla_cu_detalle.comj, " +
                "case " +
                "when tabla_cu_detalle.diaSemana = 'L' AND lunes then 1 " +
                "when tabla_cu_detalle.diaSemana = 'M' AND martes then 2 " +
                "when tabla_cu_detalle.diaSemana = 'X' AND miercoles then 3 " +
                "when tabla_cu_detalle.diaSemana = 'J' AND jueves then 4 " +
                "when tabla_cu_detalle.diaSemana = 'V' AND viernes then 5 " +
                "when tabla_cu_detalle.diaSemana = 'S' AND sabado then 6 " +
                "when tabla_cu_detalle.diaSemana = 'D' AND domingo then 7 " +
                "when tabla_cu_detalle.diaSemana = 'F' AND festivo then 8 " +
                "else 0 " +
                "end indicador, tabla_equivalencias.equivalencia " +
                "from tabla_cu_detalle " +
                "left join tabla_gr_excel_if on tabla_cu_detalle.turno = tabla_gr_excel_if.numeroTurno " +
                "left join tabla_gr_graficos on tabla_gr_excel_if.idGrafico = tabla_gr_graficos.idGrafico " +
                "left join tabla_equivalencias on tabla_gr_graficos.idGrafico = tabla_equivalencias.idGrafico " +
                "AND tabla_equivalencias.turno = tabla_cu_detalle.turno " +
                "where " +
                "tabla_cu_detalle.fecha = :fechaElegida AND " +
                "(:fechaElegida between tabla_gr_graficos.fechaInicio AND tabla_gr_graficos.fechaFinal OR " +
                "tabla_gr_graficos.idGrafico is null " +//NO HAY NINGÚN GRÁFICO BAJADO.
                ") " +
                "order by indicador desc limit 1"
    )
    fun getTurnoDeUnDia(fechaElegida: Int?): Flow<TurnoPrxTr?>


    @Query(
        "select tabla_gr_excel_if.turnoReal as turno, tabla_gr_excel_if.numeroTurno as numeroTurno, tabla_gr_excel_if.idGrafico as idGrafico, " +
                " tabla_gr_excel_if.sitioOrigen as sitioOrigen, tabla_gr_excel_if.horaOrigen as horaOrigen, tabla_gr_excel_if.sitioFin as sitioFin, " +
                "tabla_gr_excel_if.horaFin as horaFin,  " +
                "lunes || martes || miercoles || jueves || viernes || sabado || domingo || festivo as diaSemana " +
                ", tabla_equivalencias.equivalencia, '' as nota " +
                "from  tabla_gr_excel_if  " +
                "left join tabla_gr_graficos on tabla_gr_excel_if.idGrafico = tabla_gr_graficos.idGrafico " +
                "left join tabla_equivalencias on tabla_gr_graficos.idGrafico = tabla_equivalencias.idGrafico " +
                "AND tabla_equivalencias.turno = tabla_gr_excel_if.turnoReal " +
                "where " +
                "(tabla_gr_excel_if.turnoReal = :turno OR :turno = 'todo' OR :turno = tabla_equivalencias.equivalencia ) AND " +
                "tabla_gr_graficos.idGrafico = :idGrafico AND " +
                "(:diaSemana = 'L' AND lunes OR " +
                ":diaSemana = 'M' AND martes OR " +
                ":diaSemana = 'X' AND miercoles OR " +
                ":diaSemana = 'J' AND jueves OR " +
                ":diaSemana = 'V' AND viernes OR " +
                ":diaSemana = 'S' AND sabado OR " +
                ":diaSemana = 'D' AND domingo OR " +
                ":diaSemana = 'F' AND festivo OR " +
                ":diaSemana = 'todo') " +
                "order by tabla_gr_excel_if.turnoReal asc, tabla_gr_excel_if.numeroTurno asc "
    )
    fun getTurnoBuscado(
        idGrafico: Long?,
        turno: String?,
        diaSemana: String?
    ): Flow<List<TurnoBuscador>>

    @Query(
        "select distinct exif.turnoReal as turno, exif.numeroTurno as numeroTurno, exif.idGrafico as idGrafico, " +
                " exif.sitioOrigen as sitioOrigen, exif.horaOrigen as horaOrigen, exif.sitioFin as sitioFin, " +
                "exif.horaFin as horaFin,  " +
                "lunes || martes || miercoles || jueves || viernes || sabado || domingo || festivo as diaSemana, " +
                " tabla_equivalencias.equivalencia, us.name || ' ' || us.surname as nombreCompi, us.id as idCompi, " +
                " '' as nota  " +
                "from  tabla_gr_excel_if as exif " +
                "left join tabla_gr_graficos on exif.idGrafico = tabla_gr_graficos.idGrafico " +
                "left join tabla_equivalencias on tabla_gr_graficos.idGrafico = tabla_equivalencias.idGrafico " +
                "AND tabla_equivalencias.turno = exif.turnoReal " +
                "left join tabla_turno_compi as tc on tc.fecha = :hoy AND tc.turno = exif.turnoReal " +
                "left join LsUsers as us on us.id = tc.idUsuario " +
                "where " +
                "(exif.turnoReal = :turno OR :turno = 'todo' OR :turno = tabla_equivalencias.equivalencia ) AND " +
                "tabla_gr_graficos.idGrafico = :idGrafico AND " +
                "(:diaSemana = 'L' AND exif.lunes OR " +
                ":diaSemana = 'M' AND exif.martes OR " +
                ":diaSemana = 'X' AND exif.miercoles OR " +
                ":diaSemana = 'J' AND exif.jueves OR " +
                ":diaSemana = 'V' AND exif.viernes OR " +
                ":diaSemana = 'S' AND exif.sabado OR " +
                ":diaSemana = 'D' AND exif.domingo OR " +
                ":diaSemana = 'F' AND exif.festivo OR " +
                ":diaSemana = 'todo') " +
                "AND exif.turnoReal != 'D' " + //Quito descansos.
                "order by case " +
                "when :orden = 0 THEN exif.turnoReal " +
                "when :orden = 1 THEN exif.horaOrigen " +
                "end asc, " +
                "case " +
                "when :orden = 2 THEN us.surname is not null " +
                "end desc, " +
                "case " +
                "when :orden = 2 THEN us.surname " +
                "end asc, " +
                "exif.turnoReal, exif.numeroTurno asc "
    )
    fun getTurnoBuscado(
        idGrafico: Long?,
        turno: String?,
        diaSemana: String?,
        orden: Int,
        hoy: Long
    ): Flow<List<TurnoBuscador>>

    @Query(
        "Select * From tabla_gr_tareas " +
                "where idGrafico = :idGrafico " +
                "AND (turno = :turno OR :turno ='todo') " +
                " AND (diaSemana LIKE '%' || :diaSemana || '%' OR :diaSemana = 'todo') " +
                "order by turno asc, diaSemana asc, ordenServicio asc"
    )
    fun getTareasTurnoBuscado(
        idGrafico: Long?,
        turno: String?,
        diaSemana: String?
    ): Flow<List<GrTareas>>

    @Query(
        "select distinct exif1.turnoReal as turno, exif1.numeroTurno as numeroTurno, exif1.idGrafico as idGrafico, " +
                " exif1.sitioOrigen as sitioOrigen, exif1.horaOrigen as horaOrigen, exif1.sitioFin as sitioFin, " +
                "exif1.horaFin as horaFin,  " +
                "lunes || martes || miercoles || jueves || viernes || sabado || domingo || festivo as diaSemana " +
                ", tabla_equivalencias.equivalencia, '' as nota  " +
                "from  tabla_gr_excel_if as exif1  " +
                "left join tabla_gr_graficos as gr1 on exif1.idGrafico = gr1.idGrafico " +
                "left join tabla_equivalencias on gr1.idGrafico = tabla_equivalencias.idGrafico " +
                "AND tabla_equivalencias.turno = exif1.turnoReal " +
                "where " +
                "exif1.id in (" +
                "Select exif.id From tabla_gr_excel_if as exif " +
                "left join tabla_gr_tareas as grtr " +
                "ON exif.turnoReal = grtr.turno AND exif.idGrafico = grtr.idGrafico AND (" +
                "diaSemana LIKE '%' || 'L' || '%' AND lunes OR " +
                "diaSemana LIKE '%' || 'M' || '%' AND martes OR " +
                "diaSemana LIKE '%' || 'X' || '%' AND miercoles OR " +
                "diaSemana LIKE '%' || 'J' || '%' AND jueves OR " +
                "diaSemana LIKE '%' || 'V' || '%' AND viernes OR " +
                "diaSemana LIKE '%' || 'S' || '%' AND sabado OR " +
                "diaSemana LIKE '%' || 'D' || '%' AND domingo OR " +
                "diaSemana LIKE '%' || 'F' || '%' AND festivo " +
                ") " +
                "where exif.idGrafico = :idGrafico " +
                "AND (grtr.servicio like '%' || :tren || '%' OR grtr.observaciones like '%' || :tren || '%') " +//También miramos que esté en las observaciones.
                " AND (diaSemana LIKE '%' || :diaSemana || '%' OR :diaSemana = 'todo') " +
                ") AND " +
                "gr1.idGrafico = :idGrafico AND " +
                "(:diaSemana = 'L' AND lunes OR " +
                ":diaSemana = 'M' AND martes OR " +
                ":diaSemana = 'X' AND miercoles OR " +
                ":diaSemana = 'J' AND jueves OR " +
                ":diaSemana = 'V' AND viernes OR " +
                ":diaSemana = 'S' AND sabado OR " +
                ":diaSemana = 'D' AND domingo OR " +
                ":diaSemana = 'F' AND festivo OR " +
                ":diaSemana = 'todo')  "
    )
    fun getTurnoBuscadoPorTren(
        idGrafico: Long?,
        tren: String?,
        diaSemana: String?
    ): Flow<List<TurnoBuscador>>


    @Query(
        "SELECT cud.idDetalle, cud.idUsuario, cud.fecha, " +
                "  cud.turno, cud.tipo, cud.notas, cud.nombreDebe," +
                "  gr.idGrafico, exif.sitioOrigen, " +
                "  exif.horaOrigen, exif.sitioFin, exif.horaFin, " +
                "  cud.diaSemana, cud.libra, cud.comj, " +
                "  MAX(CASE " +
                "    WHEN cud.diaSemana = 'L' AND lunes THEN 1 " +
                "    WHEN cud.diaSemana = 'M' AND martes THEN 2 " +
                "    WHEN cud.diaSemana = 'X' AND miercoles THEN 3 " +
                "    WHEN cud.diaSemana = 'J' AND jueves THEN 4 " +
                "    WHEN cud.diaSemana = 'V' AND viernes THEN 5 " +
                "    WHEN cud.diaSemana = 'S' AND sabado THEN 6 " +
                "    WHEN cud.diaSemana = 'D' AND domingo THEN 7 " +
                "    WHEN cud.diaSemana = 'F' AND festivo THEN 8 " +
                "    ELSE 0 " +
                "  END) AS indicador, " +
                "  tabla_equivalencias.equivalencia, " +
                "(SELECT ch.color " +
                "   FROM tabla_colores_hora_turnos AS ch " +
                "   WHERE ch.horaInicio = ( " +
                "     SELECT MIN(ch2.horaInicio) " +
                "     FROM tabla_colores_hora_turnos AS ch2 " +
                "     WHERE ch2.horaInicio > exif.horaOrigen " +
                "   )" +
                "   LIMIT 1) AS color " +
                "FROM tabla_cu_detalle AS cud " +
                "LEFT JOIN tabla_gr_excel_if AS exif ON cud.turno = exif.numeroTurno " +
                "LEFT JOIN tabla_gr_graficos AS gr ON exif.idGrafico = gr.idGrafico " +
                "LEFT JOIN tabla_equivalencias ON gr.idGrafico = tabla_equivalencias.idGrafico " +
                "  AND tabla_equivalencias.turno = cud.turno " +
                "WHERE cud.fecha BETWEEN :fechaInicio AND :fechaFinal AND " +
                "  (cud.fecha BETWEEN gr.fechaInicio AND gr.fechaFinal OR " +
                "  gr.idGrafico IS NULL) " +
                "GROUP BY cud.fecha " +
                "ORDER BY cud.fecha "
    )
    fun getTurnosEntreFechas(fechaInicio: Long?, fechaFinal: Long?): Flow<List<TurnoPrxTr>>

    @Query(
        "select numeroTurno as turno, " +
                "case " +
                "when :diaSemana = 'L' AND lunes then 1 " +
                "when :diaSemana = 'M' AND martes then 2 " +
                "when :diaSemana = 'X' AND miercoles then 3 " +
                "when :diaSemana = 'J' AND jueves then 4 " +
                "when :diaSemana = 'V' AND viernes then 5 " +
                "when :diaSemana = 'S' AND sabado then 6 " +
                "when :diaSemana = 'D' AND domingo then 7 " +
                "when :diaSemana = 'F' AND festivo then 8 " +
                "else 0 " +
                "end indicador from  " +
                "tabla_gr_excel_if  " +
                "left join tabla_gr_graficos on tabla_gr_excel_if.idGrafico = tabla_gr_graficos.idGrafico " +
                "left join tabla_equivalencias on tabla_gr_graficos.idGrafico = tabla_equivalencias.idGrafico " +
                "AND tabla_equivalencias.turno = tabla_gr_excel_if.numeroTurno " +
                "where " +
                "tabla_gr_graficos.idGrafico = :idGrafico AND " +
                "tabla_equivalencias.equivalencia = :equivalencia AND " +
                "indicador > 0 " + //Coincide el día de la semana.
                "limit 1"
    )
     fun getTurnoDeEquivalencia(
        equivalencia: String,
        idGrafico: Long?,
        diaSemana: String?
    ): Flow<TurnoDeEquivalencia?>

    @Query(
        "Select tabla_gr_excel_if.turnoReal as turno, tabla_gr_excel_if.horaOrigen, " +
                "tabla_gr_excel_if.sitioOrigen, tabla_gr_excel_if.horaFin, tabla_gr_excel_if.sitioFin, " +
                "tabla_gr_graficos.idGrafico, tabla_gr_graficos.descripcion as desc_grafico, " +
                "case " +
                " when :diaSemana = 'L' AND lunes then 1 " +
                " when :diaSemana = 'M' AND martes then 2 " +
                " when :diaSemana = 'X' AND miercoles then 3 " +
                " when :diaSemana = 'J' AND jueves then 4 " +
                " when :diaSemana = 'V' AND viernes then 5 " +
                " when :diaSemana = 'S' AND sabado then 6 " +
                " when :diaSemana = 'D' AND domingo then 7 " +
                " when :diaSemana = 'F' AND festivo then 8 " +
                " else 0 " +
                " end indicador, equivalencia " +
                "from tabla_gr_excel_if " +
                "left join tabla_gr_graficos on tabla_gr_excel_if.idGrafico = tabla_gr_graficos.idGrafico " +
                "left join tabla_equivalencias on tabla_gr_graficos.idGrafico = tabla_equivalencias.idGrafico " +
                "AND tabla_equivalencias.turno = :turno " +
                "where " +
                ":fecha between tabla_gr_graficos.fechaInicio and tabla_gr_graficos.fechaFinal " +
                "and indicador > 0 " +
                "and turnoReal = :turno "
    )
    fun getTurnoDeUnCompi(fecha: Long?, turno: String?, diaSemana: String?): Flow<TurnoDeCompi>

    @Query(
        "Select * " +
                "from tabla_turno_compi " +
                "where fecha = :fecha and idUsuario = :idUsuario"
    )
    fun getTurnoDeUnCompi(fecha: Long?, idUsuario: Long?): Flow<TurnoCompi?>

    @Query(
        "Select COALESCE(" +
                "(SELECT turno FROM tabla_turno_compi " +
                "WHERE fecha = :fecha AND idUsuario = :idUsuario), :turnoAlternativo" +
                ") as turno, tipo " +
                "from tabla_turno_compi " +
                "where fecha = :fecha and idUsuario = :idUsuario"
    )
    fun getTurnoYTipoDeUnCompi(
        fecha: Long?,
        idUsuario: Long?,
        turnoAlternativo: String?
    ): Flow<TurnoYTipo>

    @Query(
        "Select case when exists (select * from tabla_ot_festivos where fecha = :dia) then 'F' " +
                "when :dia % 7 = 0 then 'J' " +
                "when :dia % 7 = 1 then 'V' " +
                "when :dia % 7 = 2 then 'S' " +
                "when :dia % 7 = 3 then 'D' " +
                "when :dia % 7 = 4 then 'L' " +
                "when :dia % 7 = 5 then 'M' " +
                "when :dia % 7 = 6 then 'X' " +
                "end"
    )
    fun diaSemanaFromDay(dia: Long?): Flow<String?>

    //Sumo 0 para que no dé null cuando solo hay excesosGrafico.
    @Query(
        "select (0 + sum(excesos)+ sum(mermas)+ sum(excesosGrafico)) from tabla_cu_detalle " +
                "where fecha between :inicio and :fin "
    )
    fun getMinutosExcedidos(inicio: Long, fin: Long): Flow<Long?>

    @Query(
        "select count(*) from tabla_cu_detalle where tipo = 'DCOM' " +
                "and fecha between :inicio and :fin "
    )
    fun getDcomsUsados(inicio: Long, fin: Long): Flow<Long?>

    @Query(
        "Select idGrafico from tabla_gr_graficos where fechaInicio <= :fechaElegida AND " +
                "fechaFinal >= :fechaElegida LIMIT 1"
    )
    fun getIdGraficoDeUnDia(fechaElegida: Long): Flow<Long?>

    @Query(
        "Select idGrafico from tabla_gr_graficos where fechaInicio <= :fechaElegida AND " +
                "fechaFinal >= :fechaElegida LIMIT 1"
    )
    fun getIdGraficoDeUnDiaFlow(fechaElegida: Long): Flow<Long>

    @Query(
        "Select tabla_cu_detalle.idDetalle, tabla_cu_detalle.fecha, tabla_cu_detalle.tipo," +
                "tabla_cu_detalle.turno, tabla_cu_detalle.nombreDebe, tabla_cu_detalle.notas, " +
                "tabla_ot_festivos.idFestivo, tabla_ot_festivos.Descripcion, " +
                "tabla_cu_detalle.libra, tabla_cu_detalle.comj, tabla_cu_detalle.mermas, " +
                "tabla_cu_detalle.excesos " +
                "FROM tabla_cu_detalle left join tabla_ot_festivos " +
                "on tabla_cu_detalle.fecha = tabla_ot_festivos.fecha " +
                "where tabla_cu_detalle.fecha = :fecha"
    )
    fun getCuDetallesDeUnDia(fecha: Int?): Flow<CuDetalleConFestivoDBModel?>


    @Query(
        "SELECT tabla_cu_detalle.idDetalle, tabla_cu_detalle.fecha, tabla_cu_detalle.tipo," +
                "tabla_cu_detalle.turno, tabla_cu_detalle.nombreDebe, tabla_cu_detalle.notas, " +
                "tabla_ot_festivos.idFestivo, tabla_ot_festivos.Descripcion, libra,comj, mermas,excesos " +
                "FROM tabla_cu_detalle left join tabla_ot_festivos " +
                "on tabla_cu_detalle.fecha = tabla_ot_festivos.fecha " +
                "where tabla_cu_detalle.fecha between :fechaInicial and :fechaFinal " +
                "GROUP BY tabla_cu_detalle.idDetalle "
    )
    fun getCuDetallesConFestivos(
        fechaInicial: Long?,
        fechaFinal: Long?
    ): Flow<List<CuDetalleConFestivoDBModel>>


    /** ACCIONES RELACIONADAS CON TABLAS INDIVIDUALES **/
    //////////////////////GR_listado_graficos (Yo lo llamaré GrGraficos)
    @Upsert()
    suspend fun upsertGrafico(graficos: GrGraficos)

    @Query("Select * from tabla_gr_graficos order by fechaInicio DESC")
    fun getGraficos(): Flow<List<GrGraficos>>

    @Query("Select * from tabla_gr_graficos order by fechaInicio ASC")
    fun getGraficosRaw(): Flow<List<GrGraficos>>


    @Query(
        "Select * from tabla_gr_graficos " +
                "where idGrafico in (select idGrafico from tabla_gr_excel_if) " +
                "order by fechaInicio DESC"
    )
    fun getGraficosDescargados(): Flow<List<GrGraficos>>

    @Query(
        "Select gr.*, " +
                "case when exists (" +
                "select 1 from tabla_gr_excel_if where tabla_gr_excel_if.idGrafico = gr.idGrafico" +
                ") then 1 else 0 end as descargado " +
                "from tabla_gr_graficos as gr " +
                "order by fechaInicio desc"
    )
    fun getGraficosConVar(): Flow<List<GrGraficosConVar>>

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
                "where idGrafico = :id " +
                "limit 1"
    )
    fun getGraficoDeUnId(id: Long?): Flow<GrGraficos>

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

    //////////////////////GrNotasTurno
    //Solo nos valen las de un turno, de un gráfico, en un día de la semana concreto.
    @Query(
        "Select * from tabla_notas_turno where " +
                "idGrafico = :idGrafico AND " +
                "turno = :turno AND " +
                "(:diaSemana = 'L' AND lunes OR " +
                ":diaSemana = 'M' AND martes OR " +
                ":diaSemana = 'X' AND miercoles OR " +
                ":diaSemana = 'J' AND jueves OR " +
                ":diaSemana = 'V' AND viernes OR " +
                ":diaSemana = 'S' AND sabado OR " +
                ":diaSemana = 'D' AND domingo OR " +
                ":diaSemana = 'F' AND festivo) " +
                "OR " +
                "tabla_notas_turno.idGrafico = :idGrafico and " +
                "tabla_notas_turno.turno in (SELECT equivalencia from tabla_equivalencias where " +
                "tabla_equivalencias.turno = :turno and tabla_equivalencias.idGrafico = :idGrafico)"
    )
    fun getNotasDelTurno(
        idGrafico: Long?,
        turno: String?,
        diaSemana: String?
    ): Flow<List<GrNotasTurno>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrNotasTurno(grNotasTurno: GrNotasTurno)

    @Query("Select * from tabla_notas_turno")
    fun getAllNotasTurno(): Flow<List<GrNotasTurno>>

    @Query("Select * from tabla_notas_turno where idGrafico = :idGrafico")
    fun getGrNotasTurnoFromGrafico(idGrafico: Long): Flow<List<GrNotasTurno>>

    @Query("delete from tabla_notas_turno")
    suspend fun deleteAllGrNotasTurno()

    @Query("delete from tabla_notas_turno where idGrafico = :idGrafico")
    suspend fun deleteGrNotasTurnoDelGrafico(idGrafico: Long)

    @Query("delete from tabla_notas_turno where id = :idGrafico")
    suspend fun deleteOneGrNotasTurno(idGrafico: Long)


    //////////////////////GrNotasTren
    @Query(
        "Select * from tabla_notas_tren where " +
                "idGrafico = :idGrafico AND " +
                "(:diaSemana = 'L' AND lunes OR " +
                ":diaSemana = 'M' AND martes OR " +
                ":diaSemana = 'X' AND miercoles OR " +
                ":diaSemana = 'J' AND jueves OR " +
                ":diaSemana = 'V' AND viernes OR " +
                ":diaSemana = 'S' AND sabado OR " +
                ":diaSemana = 'D' AND domingo OR " +
                ":diaSemana = 'F' AND festivo) AND " +
                "tren in (select servicio from tabla_gr_tareas WHERE " +
                "tabla_gr_tareas.turno = :turno AND " +
                "tabla_gr_tareas.idGrafico = :idGrafico AND " +
                "tabla_gr_tareas.diaSemana LIKE '%' || :diaSemana || '%' ) "
    )
    fun getNotasTrenDelTurno(
        idGrafico: Long?,
        turno: String?,
        diaSemana: String?
    ): Flow<List<GrNotasTren>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrNotasTren(grNotasTren: GrNotasTren)

    @Query("Select * from tabla_notas_tren where idGrafico = :key")
    fun getGrNotasTrenFromGrafico(key: Long): Flow<List<GrNotasTren>>

    @Query("delete from tabla_notas_tren")
    suspend fun deleteAllGrNotasTren()

    @Query("delete from tabla_notas_tren where idGrafico = :key")
    suspend fun deleteGrNotasTrenDelGrafico(key: Long)

    @Query("delete from tabla_notas_tren where id = :key")
    suspend fun deleteOneGrNotasTren(key: Long)

    //////////////////////GrEquivalencias
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrEquivalencias(grEquivalencias: GrEquivalencias)

    @Query("Select * from tabla_equivalencias where idGrafico = :key")
    fun getEquivalenciasFromGrafico(key: Long): Flow<List<GrEquivalencias>>

    @Query(
        "Select equivalencia from tabla_equivalencias where " +
                "idGrafico = :idGrafico AND turno = :turno"
    )
    fun getOneEquivalencia(idGrafico: Long, turno: String): Flow<String?>

    @Query(
        "Select equivalencia from tabla_equivalencias as te " +
                "left join tabla_gr_graficos as tg on te.idGrafico = tg.idGrafico " +
                "where " +
                ":date between tg.fechaInicio and tg.fechaFinal AND turno = :turno"
    )
    fun getOneEquivalenciaOfDate(date: Long?, turno: String?): Flow<String?>

    @Query("delete from tabla_equivalencias")
    suspend fun deleteAllGrEquivalencias()

    @Query("delete from tabla_equivalencias where idGrafico = :key")
    suspend fun deleteGrEquivalenciasDelGrafico(key: Long)


    //////////////////////GR_detalles_mensual (Yo lo llamaré GrExcelIF)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrExcelIF(grExcelIF: GrExcelIF)

    @Query("Select EXISTS(Select * from tabla_gr_excel_if where idGrafico = :key limit 1)")
    fun graficoTieneExcelIF(key: Long): Flow<Boolean>

    @Query(
        "Select " +
                "EXISTS(Select tabla_gr_excel_if.id from tabla_gr_excel_if " +
                "left join tabla_gr_graficos on tabla_gr_excel_if.idGrafico = tabla_gr_graficos.idGrafico " +
                "where :fecha between tabla_gr_graficos.fechaInicio and tabla_gr_graficos.fechaFinal limit 1)"
    )
    fun fechaTieneExcelIF(fecha: Long?): Flow<Boolean>

    @Query(
        "select exists (select * from tabla_gr_excel_if " +
                "where idGrafico = :idGrafico and turnoReal = :turno)"
    )
    fun elTurnoTieneExcelIF(turno: String?, idGrafico: Long?): Flow<Boolean>

    @Query("Select * from tabla_gr_excel_if where idGrafico = :key")
    fun getGrExcelIFFromGrafico(key: Long): Flow<List<GrExcelIF>>

    @Query("delete from tabla_gr_excel_if")
    suspend fun deleteAllGrExcelIF()

    @Query("delete from tabla_gr_excel_if where idGrafico = :id")
    suspend fun deleteGrExcelIF(id: Long?)


    //////////////////////GR_detalles_libreta (Yo lo llamaré GrTareas)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrTareas(grTareas: GrTareas)


    @Query("Select * from tabla_gr_tareas where idGrafico = :key")
    fun getGrTareasFromGrafico(key: Long): Flow<List<GrTareas>>

    @Query("Select * from tabla_gr_tareas where idGrafico = :key LIMIT 1")
    fun getOneGrTareasFromGrafico(key: Long): Flow<GrTareas?>

    @Query("Select * from tabla_gr_tareas where idGrafico = :key LIMIT 1")
    fun areThereTareasFromGrafico(key: Long): Flow<GrTareas?>

    @Query("delete from tabla_gr_tareas")
    suspend fun deleteAllGrTareas()

    @Query("delete from tabla_gr_tareas where idGrafico = :idGrafico")
    suspend fun deleteAGrTareas(idGrafico: Long?)

    @Query(
        "Select * From tabla_gr_tareas " +
                "where idGrafico = :idGrafico " +
                " AND turno = :turno " +
                " AND diaSemana LIKE '%' || :diaSemana || '%' " +
                "order by ordenServicio asc"
    )
    fun getTareasDeUnTurno(
        idGrafico: Long?,
        turno: String?,
        diaSemana: String?
    ): Flow<List<GrTareas>>

    @Query(
        "Select id, idGrafico, turno, ordenServicio, servicio, tipoServicio, diaSemana, " +
                "sitioOrigen, horaOrigen, sitioFin, horaFin, vehiculo, observaciones, inserted, " +
                "(Select group_concat(nota) from tabla_notas_tren as nt where " +
                "gr.servicio = nt.tren and " +
                "idGrafico = :idGrafico AND " +
                "(:diaSemana = 'L' AND lunes OR " +
                ":diaSemana = 'M' AND martes OR " +
                ":diaSemana = 'X' AND miercoles OR " +
                ":diaSemana = 'J' AND jueves OR " +
                ":diaSemana = 'V' AND viernes OR " +
                ":diaSemana = 'S' AND sabado OR " +
                ":diaSemana = 'D' AND domingo OR " +
                ":diaSemana = 'F' AND festivo) AND " +
                "tren in (select servicio from tabla_gr_tareas WHERE " +
                "tabla_gr_tareas.turno = :turno AND " +
                "tabla_gr_tareas.idGrafico = :idGrafico AND " +
                "tabla_gr_tareas.diaSemana LIKE '%' || :diaSemana || '%' ) " +
                ") as notasTren " +
                // "te2.latitud as latFin, te2.longitud as longFin " +
                "From tabla_gr_tareas as gr " +
                //  "left join tabla_clima as cl1 on cl1.estacion = gr.sitioOrigen AND " +
                "where gr.idGrafico = :idGrafico " +
                " AND gr.turno = :turno " +
                " AND gr.diaSemana LIKE '%' || :diaSemana || '%' " +
                "order by ordenServicio asc"
    )
    fun getTareasDeUnTurnoDM(
        idGrafico: Long?,
        turno: String?,
        diaSemana: String?
    ): Flow<List<GrTareaConClima>>

    @Query(
        "Select * From tabla_gr_tareas " +
                "where idGrafico = :idGrafico " +
                " AND turno = :turno " +
                " AND diaSemana LIKE '%' || :diaSemana || '%' " +
                "order by ordenServicio asc"
    )
    fun getTareasDeUnTurnoRaw(
        idGrafico: Long?,
        turno: String?,
        diaSemana: String?
    ): Flow<List<GrTareas>>

    @Query(
        "Select id, idGrafico, turno, ordenServicio, servicio, tipoServicio, diaSemana," +
                "e1.acronimo as sitioOrigen, horaOrigen, e2.acronimo as sitioFin, horaFin, " +
                "vehiculo, observaciones, inserted From tabla_gr_tareas " +
                "left join tabla_estaciones as e1 on sitioOrigen = e1.nombre " +
                "left join tabla_estaciones as e2 on sitioFin = e2.nombre " +
                "where idGrafico = :idGrafico " +
                " AND turno = :turno " +
                " AND diaSemana LIKE '%' || :diaSemana || '%' " +
                "order by ordenServicio asc"
    )
    fun getTareasCortasDeUnTurno(
        idGrafico: Long?,
        turno: String?,
        diaSemana: String?
    ): Flow<List<GrTareas>>

    @Query(
        "Select id, idGrafico, turno, ordenServicio, servicio, tipoServicio, diaSemana, " +
                "sitioOrigen,e1.acronimo as sOrigenCorto, horaOrigen, " +
                "sitioFin, e2.acronimo as sFinCorto, horaFin, " +
                "vehiculo, observaciones, inserted, " +
                "(Select group_concat(nota) from tabla_notas_tren as nt " +
                "where " +
                "gr.servicio = nt.tren and " +
                "idGrafico = :idGrafico AND " +
                "(:diaSemana = 'L' AND lunes OR " +
                ":diaSemana = 'M' AND martes OR " +
                ":diaSemana = 'X' AND miercoles OR " +
                ":diaSemana = 'J' AND jueves OR " +
                ":diaSemana = 'V' AND viernes OR " +
                ":diaSemana = 'S' AND sabado OR " +
                ":diaSemana = 'D' AND domingo OR " +
                ":diaSemana = 'F' AND festivo) AND " +
                "tren in (select servicio from tabla_gr_tareas WHERE " +
                "tabla_gr_tareas.turno = :turno AND " +
                "tabla_gr_tareas.idGrafico = :idGrafico AND " +
                "tabla_gr_tareas.diaSemana LIKE '%' || :diaSemana || '%' ) " +
                ") as notasTren " +
                // "te2.latitud as latFin, te2.longitud as longFin " +
                "From tabla_gr_tareas as gr " +
                "left join tabla_estaciones as e1 on sitioOrigen = e1.nombre " +
                "left join tabla_estaciones as e2 on sitioFin = e2.nombre " +
                //  "left join tabla_clima as cl1 on cl1.estacion = gr.sitioOrigen AND " +
                "where gr.idGrafico = :idGrafico " +
                " AND gr.turno = :turno " +
                " AND gr.diaSemana LIKE '%' || :diaSemana || '%' " +
                "order by ordenServicio asc"
    )
    fun getTareasDeUnTurnoBuscador2(
        idGrafico: Long?,
        turno: String?,
        diaSemana: String?
    ): Flow<List<GrTareaBuscador>>

    @Query(
        "Select exists (Select * From tabla_gr_tareas " +
                "where idGrafico = :idGrafico " +
                " AND turno = :turno " +
                "limit 1)"
    )
    fun getTareasDeUnTurno(
        idGrafico: Long?,
        turno: String?
    ): Flow<Boolean>

    ///////////////////// OT_Estaciones
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstacion(estaciones: Estaciones)

    @Upsert()
    suspend fun upsertEstacion(estacionWCoordinates: Estaciones)

    @Query("select exists (select * from tabla_estaciones)")
    fun hayEstaciones(): Flow<Boolean>

    @Query(
        "Select * from tabla_estaciones where nombre in " +
                "(Select distinct * from (" +
                "select distinct sitioOrigen from tabla_gr_tareas " +
                "union " +
                "select distinct sitioFin from tabla_gr_tareas) " +
                "as combined_column)"
    )
    fun getEstacionesDeGraficos(): Flow<List<Estaciones>>

    @Query(
        "Select distinct * from (" +
                "select distinct sitioOrigen from tabla_gr_tareas " +
                "union " +
                "select distinct sitioFin from tabla_gr_tareas) " +
                "as combined_column"
    )
    fun getEstacionesEnGraficos(): Flow<List<String>>

    @Query("Select exists (select * from tabla_estaciones where nombre = :nombre)")
    fun isStationInEstaciones(nombre: String): Flow<Boolean>


    @Query("Select * from tabla_estaciones")
    fun getAllEstaciones(): Flow<List<Estaciones>>

    @Query(
        "update tabla_estaciones " +
                "set longitud = :longitud, latitud = :lat " +
                "where nombre = :nombre"
    )
    suspend fun setStationCoordinates(longitud: Float, lat: Float, nombre: String)

    @Query("Select * from tabla_estaciones where esDelGrafico and latitud is null")
    fun getChartStationsWithoutCoordinates(): Flow<List<Estaciones>>


    //////////////////////OT_teleindicadores
//    @Query(
//        "select * from tabla_teleindicadores where " +
//                "(:diaSemana = 'L' AND lunes OR " +
//                ":diaSemana = 'M' AND martes OR " +
//                ":diaSemana = 'X' AND miercoles OR " +
//                ":diaSemana = 'J' AND jueves OR " +
//                ":diaSemana = 'V' AND viernes OR " +
//                ":diaSemana = 'S' AND sabado OR " +
//                ":diaSemana = 'D' AND domingo OR " +
//                ":diaSemana = 'F' AND festivo) AND " +
//                "tren in (select servicio from tabla_gr_tareas WHERE " +
//                "tabla_gr_tareas.turno = :turno AND " +
//                "tabla_gr_tareas.idGrafico = :idGrafico AND " +
//                "tabla_gr_tareas.diaSemana LIKE '%' || :diaSemana || '%' ) " +
//                "order by tabla_teleindicadores.tren asc, tabla_teleindicadores.vehiculo asc"
//    )
//    fun getTeleindicadores(
//        idGrafico: Long?,
//        turno: String?,
//        diaSemana: String?
//    ): Flow<List<OtTeleindicadores?>>
    @Query(
        "select ti.id, ti.tren, ti.codigo,ti.vehiculo, ti.notas, ti.lunes, ti.martes, ti.miercoles, ti.jueves, " +
                "ti.viernes, ti.sabado, ti.domingo, ti.festivo from tabla_teleindicadores as ti " +
                "left join tabla_gr_tareas as tr on ti.tren = tr.servicio where " +
                "(:diaSemana = 'L' AND lunes OR " +
                ":diaSemana = 'M' AND martes OR " +
                ":diaSemana = 'X' AND miercoles OR " +
                ":diaSemana = 'J' AND jueves OR " +
                ":diaSemana = 'V' AND viernes OR " +
                ":diaSemana = 'S' AND sabado OR " +
                ":diaSemana = 'D' AND domingo OR " +
                ":diaSemana = 'F' AND festivo) AND " +
                "tren in (select servicio from tabla_gr_tareas WHERE " +
                "tabla_gr_tareas.turno = :turno AND " +
                "tabla_gr_tareas.idGrafico = :idGrafico AND " +
                "tabla_gr_tareas.diaSemana LIKE '%' || :diaSemana || '%' ) " +
                "group by ti.vehiculo, ti.tren, ti.codigo " +
                "order by tr.ordenServicio asc, tr.horaOrigen asc, ti.vehiculo asc, ti.tren asc "
    )
    fun getTeleindicadores(
        idGrafico: Long?,
        turno: String?,
        diaSemana: String?
    ): Flow<List<OtTeleindicadores>>

    @Query(
        "Select * from tabla_teleindicadores where :tren LIKE tren AND codigo != '' " +
                "OR :tren ='todo' AND codigo != '' " +
                "order by tren, vehiculo asc "
    )
    fun getTeleindicadoresPorTren(tren: String): Flow<List<OtTeleindicadores>>

    @Query("Select exists (Select * from tabla_teleindicadores where codigo != '' limit 1)")
    fun hayTeleindicadores(): Flow<Boolean>

    @Query("Select * from tabla_teleindicadores")
    fun getAllTeleindicadores(): Flow<List<OtTeleindicadores?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeleindicador(teleindicadores: OtTeleindicadores)

    @Query("Delete From tabla_teleindicadores")
    suspend fun deleteAllTeleindicadores()

    //////////////////////OT_festivos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserAllOtFestivos(otFestivos: List<OtFestivo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOtFestivos(otFestivos: OtFestivo)

    @Query("Select Descripcion from tabla_ot_festivos where fecha = :fecha")
    fun getFestivoDeUnDia(fecha: Int?): Flow<String?>

    @Query("Select * From tabla_ot_festivos order by fecha asc")
    fun getOtFestivos(): Flow<List<OtFestivo>>

    @Query("Select * From tabla_ot_festivos order by idFestivo desc")
    fun getAllOtFestivos(): Flow<List<OtFestivo>>

    @Query("Delete From tabla_ot_festivos")
    suspend fun deleteAllOtFestivos()


    ///////////////////////CuDetalle
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCuDetalles(cuDetalles: CuDetalle)

    @Query("Select * From tabla_cu_detalle")
    fun getCuDetalles(): Flow<List<CuDetalle>>

    @Query("Delete From tabla_cu_detalle")
    suspend fun deleteAllCuDetalles()

    @Query(
        "update tabla_cu_detalle " +
                "set excesosGrafico = :minutos " +
                "where fecha = :date"
    )
    suspend fun updateExcesoGrafico(date: Int?, minutos: Int?)

    @Query(
        "update tabla_cu_detalle " +
                "set excesosGrafico = 0 " +
                "where fecha between :inicio and :fin "
    )
    suspend fun clearExcesosGrafico(inicio: Long, fin: Long)

    @Query(
        "select idDetalle  " +
                "from tabla_cu_detalle " +
                "where excesosGrafico != 0 "
    )
    fun getExcesosGrafico(): Flow<List<Long>>

    @Query(
        "select excesosGrafico  " +
                "from tabla_cu_detalle " +
                "where idDetalle = :id "
    )
    fun getExcesosGraficoFromId(id: Long): Flow<Int?>

    @Query(
        "Select * from tabla_cu_detalle " +
                "where (excesos != 0 OR mermas != 0 or tipo = 'DCOM' or excesosGrafico != 0) " +
                "AND fecha between :inicio and :fin " +
                "order by fecha desc"
    )
    fun getCuDetallesConExcesos(inicio: Long, fin: Long): Flow<List<CuDetalle>>


    @Query("Select * from tabla_cu_detalle where fecha = :selectedDate")
    fun getCuDetalleDeUnDia(selectedDate: Long?): Flow<CuDetalle?>

    @Query(
        "Select tipo, count(*) as cantidad from tabla_cu_detalle " +
                "where fecha between :yearStart and :yearEnd " +
                "group by tipo " +
                "order by tipo"
    )
    fun getTurnosPorTipo(yearStart: Long, yearEnd: Long): Flow<List<TurnosPorTipo>>

    @Query(
        "Select turno as tipo, count(*) as cantidad from tabla_cu_detalle " +
                "where fecha between :yearStart and :yearEnd " +
                "group by turno " +
                "order by tipo"
    )
    fun getTurnosPorTurno(yearStart: Long, yearEnd: Long): Flow<List<TurnosPorTipo>>


    @Query(
        "Select distinct nombreDebe from tabla_cu_detalle UNION " +
                "SELECT distinct LsUsers.name || ' ' || LsUsers.surname as nombreDebe from LsUsers"
    )
    fun getUsuariosEnNombreDebe(): Flow<List<String>>

    @Query(
        "Select * from tabla_cu_detalle where " +
                "tipo = 'TC' OR tipo = 'TAD' OR tipo = 'DAD' " +
                "OR nombreDebe IS NOT NULL AND nombreDebe != '' AND nombreDebe != 'Sin nombre' " +
                "order by fecha desc"
    )
    fun getTurnosCambiados(): Flow<List<CuDetalle>>

    @Query(
        "Select * from tabla_cu_detalle where " +
                "(tipo = 'TC' OR tipo = 'TAD' OR tipo = 'DAD' " +
                "OR nombreDebe IS NOT NULL AND nombreDebe != '' AND nombreDebe != 'Sin nombre') AND " +
                "(nombreDebe = :nombre OR :todos) " +
                "order by fecha desc"
    )
    fun getTurnosCambiadosV2(nombre: String?, todos: Boolean?): Flow<List<CuDetalle>>

    @Query(
        "Select exists (" +
                "select TIF.turnoReal from tabla_gr_graficos as TG " +
                "left join tabla_gr_excel_if as TIF " +
                "on TG.idGrafico = TIF.idGrafico " +
                "where :fecha between TG.fechaInicio and TG.fechaFinal " +
                "and TIF.turnoReal = :turno)"
    )
    fun elTurnoEstaEnElGrafico(turno: String?, fecha: Long?): Flow<Boolean>

    @Query(
        "Select * from tabla_cu_detalle where " +
                "notas IS NOT NULL AND notas != '' " +
                "order by fecha desc"
    )
    fun getTurnosConNotas(): Flow<List<CuDetalle>>

    @Query(
        "Select nombreDebe, " +
                "sum(case when tipo = 'TC' then 1 else 0 end) as TC," +
                "sum(case when tipo = 'TAD' then 1 else 0 end) as TAD," +
                "sum(case when tipo = 'DAD' then 1 else 0 end) as DAD, " +
                "(sum(case when tipo = 'TAD' then 1 else 0 end) - sum(case when tipo = 'DAD' then 1 else 0 end)) as balance " +
                "from tabla_cu_detalle " +
                "where nombreDebe != '' and nombreDebe != 'Sin nombre' " +
                "group by nombreDebe " +
                "having (:soloSinDeuda = 1 AND balance != 0 OR " +
                ":soloSinDeuda = 0)"
    )
    fun getCompisConCambios(soloSinDeuda: Int): Flow<List<CuDetalleCompisConCambios>>

    @Query(
        "Select sum(t1.comj) + " +//Días ganados.
                "(select ti.valor from tabla_dias_iniciales as ti " +//Días iniciales.
                "where ti.tipo = 'COMJ' and ti.anio = :year ) - " +
                "(Select count(*) from tabla_cu_detalle as t2 where " +//Días usados.
                "t2.tipo = 'COMJ' AND " +
                "t2.fecha >= :fechaInicial AND t2.fecha <= :fechaFinal) " +
                "from tabla_cu_detalle as t1 " +
                "where t1.fecha  >= :fechaInicial AND t1.fecha <= :fechaFinal"
    )
    fun getNumComjs(fechaInicial: Long?, fechaFinal: Long?, year: Int): Flow<Int?>

    @Query(
        "Select  " +
                "(select ti.valor from tabla_dias_iniciales as ti " +//Días iniciales.
                "where ti.tipo = 'DD' and ti.anio = :year ) - " +
                "(Select count(*) from tabla_cu_detalle as t2 where " +//Días usados.
                "t2.tipo = 'DD' AND t2.turno != 'DD' AND " +
                "t2.fecha >= :fechaInicial AND t2.fecha <= :fechaFinal) " +
                "from tabla_cu_detalle as t1 " +
                "where t1.fecha  >= :fechaInicial AND t1.fecha <= :fechaFinal"
    )
    fun getNumDds(fechaInicial: Long?, fechaFinal: Long?, year: Int): Flow<Int?>

    @Query(
        "Select  " +
                "(select ti.valor from tabla_dias_iniciales as ti " +//Días iniciales.
                "where ti.tipo = 'DJ' and ti.anio = :year ) - " +
                "(Select count(*) from tabla_cu_detalle as t2 where " +//Días usados.
                "t2.tipo = 'DJ' AND " +
                "t2.fecha >= :fechaInicial AND t2.fecha <= :fechaFinal) " +
                "from tabla_cu_detalle as t1 " +
                "where t1.fecha  >= :fechaInicial AND t1.fecha <= :fechaFinal"
    )
    fun getNumDjs(fechaInicial: Long?, fechaFinal: Long?, year: Int): Flow<Int?>

    @Query(
        "Select  " +
                "(select ti.valor from tabla_dias_iniciales as ti " +//Días iniciales.
                "where ti.tipo = 'DJA' and ti.anio = :year ) - " +
                "(Select count(*) from tabla_cu_detalle as t2 where " +//Días usados.
                "t2.tipo = 'DJA' AND " +
                "t2.fecha >= :fechaInicial AND t2.fecha <= :fechaFinal) " +
                "from tabla_cu_detalle as t1 " +
                "where t1.fecha  >= :fechaInicial AND t1.fecha <= :fechaFinal"
    )
    fun getNumDjas(fechaInicial: Long?, fechaFinal: Long?, year: Int): Flow<Int?>

    @Query(
        "Select sum(t1.libra) + " +//Días ganados.
                "(select ti.valor from tabla_dias_iniciales as ti " +//Días iniciales.
                "where ti.tipo = 'LIBRa' and ti.anio = :year ) - " +
                "(Select count(*) from tabla_cu_detalle as t2 where " +//Días usados.
                "t2.tipo = 'LIBRa' AND " +
                "t2.fecha >= :fechaInicial AND t2.fecha <= :fechaFinal) " +
                "from tabla_cu_detalle as t1 " +
                "where t1.fecha  >= :fechaInicial AND t1.fecha <= :fechaFinal"
    )
    fun getNumLibras(fechaInicial: Long?, fechaFinal: Long?, year: Int): Flow<Int?>

    @Query(
        "Select " +//Días ganados.
                "(select ti.valor " +
                "from tabla_dias_iniciales as ti " +//Días iniciales.
                "where ti.tipo = 'LZ' and ti.anio = :year ) - " +
                "(Select count(*) " +
                "from tabla_cu_detalle as t2 " +
                "where " +//Días usados.
                "t2.tipo = 'LZ' AND " +
                "t2.fecha >= :fechaInicial AND t2.fecha <= :fechaFinal) " +
                "from tabla_cu_detalle as t1 " +
                "where t1.fecha  >= :fechaInicial AND t1.fecha <= :fechaFinal"
    )
    fun getNumLzs(fechaInicial: Long?, fechaFinal: Long?, year: Int): Flow<Int?>

    @Query(
        "Select " +
                "(select ti.valor " +
                "from tabla_dias_iniciales as ti " +//Días iniciales.
                "where ti.tipo = 'LZA' and ti.anio = :year ) - " +
                "(Select count(*) " +
                "from tabla_cu_detalle as t2 " +
                "where " +//Días usados.
                "t2.tipo = 'LZA' AND " +
                "t2.fecha >= :fechaInicial AND t2.fecha <= :fechaFinal) " +
                "from tabla_cu_detalle as t1 " +
                "where t1.fecha  >= :fechaInicial AND t1.fecha <= :fechaFinal"
    )
    fun getNumLzas(fechaInicial: Long?, fechaFinal: Long?, year: Int): Flow<Int?>

    @Query(
        "Select * from tabla_cu_detalle where " +
                "(tipo = 'COMJ' OR comj IS NOT NULL AND comj != '0') AND " +
                "fecha >= :fechaInicial AND fecha <= :fechaFinal"
    )
    fun getComjs(fechaInicial: Long?, fechaFinal: Long?): Flow<List<CuDetalle>>

    @Query(
        "Select * from tabla_cu_detalle where " +
                "tipo = 'DD' AND turno != 'DD' " +
                "AND fecha >= :fechaInicial AND fecha <= :fechaFinal"
    )
    fun getDDs(fechaInicial: Long?, fechaFinal: Long?): Flow<List<CuDetalle>>

    @Query(
        "Select * from tabla_cu_detalle where " +
                "(tipo = 'DJ' or turno = 'DJ')" +
                "AND fecha >= :fechaInicial AND fecha <= :fechaFinal"
    )
    fun getDJs(fechaInicial: Long?, fechaFinal: Long?): Flow<List<CuDetalle>>

    @Query(
        "Select * from tabla_cu_detalle where " +
                "(tipo = 'DJA' or turno = 'DJA')" +
                "AND fecha >= :fechaInicial AND fecha <= :fechaFinal"
    )
    fun getDJAs(fechaInicial: Long?, fechaFinal: Long?): Flow<List<CuDetalle>>

    @Query(
        "select * from tabla_cu_detalle where " +
                "(tipo = 'LIBRa' OR libra IS NOT NULL AND libra != '0') " +
                "AND fecha >= :fechaInicial AND fecha <= :fechaFinal"
    )
    fun getLIBRas(fechaInicial: Long?, fechaFinal: Long?): Flow<List<CuDetalle>>

    @Query(
        "select * from tabla_cu_detalle where " +
                "(tipo = 'LZ' or turno = 'LZ')" +
                "AND fecha >= :fechaInicial AND fecha <= :fechaFinal"
    )
    fun getLZs(fechaInicial: Long?, fechaFinal: Long?): Flow<List<CuDetalle>>

    @Query(
        "select * from tabla_cu_detalle where " +
                "(tipo = 'LZA' or turno = 'LZA')" +
                "AND fecha >= :fechaInicial AND fecha <= :fechaFinal"
    )
    fun getLZAs(fechaInicial: Long?, fechaFinal: Long?): Flow<List<CuDetalle>>

    @Query(
        "Delete from tabla_cu_detalle where " +
                "fecha between :fechaInicial and :fechaFinal"
    )
    suspend fun deleteYearOfCuDetalles(fechaInicial: Long, fechaFinal: Long)

    ////////////////////////OTDeletedElements
    //La tabla como tal no existe, pero desde aquí gestiono los borrados.
    @Query("Delete from LsUsers where id = :id")
    suspend fun deletedElementUsuario(id: Long?)

    @Query("Delete from tabla_ot_festivos where idFestivo = :id")
    suspend fun deletedElementFestivo(id: Long?)

    @Query("Delete from tabla_gr_graficos where idGrafico = :id")
    suspend fun deletedElementGrafico(id: Long?)


    ///////////////////////OTColoresTrenes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColoresTrenes(otColoresTrenes: OtColoresTrenes)

    @Query("Delete from tabla_colores_trenes")
    suspend fun deleteAllColoresTrenes()

    @Query("Select * from tabla_colores_trenes")
    fun getAllColoresTrenes(): Flow<List<OtColoresTrenes>>


    ////////////////////////OtMensajesAdmin
    @Query(
        "Select * from tabla_mensajes_de_admin where " +
                "estado != 2 AND estado != -2 AND :borrados = 0 " +
                "OR :borrados = 1"
    )
    fun getMensajesDeAdmin(borrados: Int): Flow<List<OtMensajesAdmin>>

    @Query(
        "Select count(*) from tabla_mensajes_de_admin " +
                "where estado = 0 "
    )
    fun mensajesAdminNuevos(): Flow<Int>

    @Query("Select * from tabla_mensajes_de_admin where id = :id")
    fun getUnMensajeDeAdmin(id: Long?): Flow<OtMensajesAdmin?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMensajeDeAdmin(mensajesAdmin: OtMensajesAdmin)

    @Query(
        "delete from tabla_mensajes_de_admin " +
                "where id = :id"
    )
    suspend fun deleteMensajeDeAdmin(id: Long?)

    @Update(entity = OtMensajesAdmin::class)
    suspend fun updateEstadoMensajeAdmin(otMensajesAdmin: OtMensajesAdmin)


    ////////////////////////CuDiasIniciales
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiasIniciales(cuDiasIniciales: CuDiasIniciales)

    @Query("select * from tabla_dias_iniciales ")
    fun getAllDiasIniciales(): Flow<List<CuDiasIniciales>>

    @Query(
        "select * from tabla_dias_iniciales where anio = :year AND (tipo == 'COMJ' " +
                "OR tipo == 'DD' OR tipo == 'DJ' OR tipo == 'DJA' OR tipo == 'LIBRa' OR tipo == 'LZ' " +
                "OR tipo == 'LZA') " +
                "order by tipo asc "
    )
    fun getDiasInicialesOfYear(year: Int): Flow<List<CuDiasIniciales>>

    @Query(
        "select * from tabla_dias_iniciales where anio = :year AND tipo == 'COMJ' limit 1 "
    )
    fun getComjInicialesOfYear(year: Int): Flow<CuDiasIniciales>

    @Query(
        "select * from tabla_dias_iniciales where anio = :year AND tipo == 'DD' limit 1 "
    )
    fun getDdInicialesOfYear(year: Int): Flow<CuDiasIniciales>

    @Query(
        "select * from tabla_dias_iniciales where anio = :year AND tipo == 'DJ' limit 1 "
    )
    fun getDjInicialesOfYear(year: Int): Flow<CuDiasIniciales>

    @Query(
        "select * from tabla_dias_iniciales where anio = :year AND tipo == 'DJA' limit 1 "
    )
    fun getDjaInicialesOfYear(year: Int): Flow<CuDiasIniciales>

    @Query(
        "select * from tabla_dias_iniciales where anio = :year AND tipo == 'LIBRa' limit 1 "
    )
    fun getLibraInicialesOfYear(year: Int): Flow<CuDiasIniciales>

    @Query(
        "select * from tabla_dias_iniciales where anio = :year AND tipo == 'LZ' limit 1 "
    )
    fun getLzInicialesOfYear(year: Int): Flow<CuDiasIniciales>

    @Query(
        "select * from tabla_dias_iniciales where anio = :year AND tipo == 'LZA' limit 1 "
    )
    fun getLzaInicialesOfYear(year: Int): Flow<CuDiasIniciales>

    @Query(
        "select count(*) from tabla_dias_iniciales " +
                "where anio = :year and tipo != 'DCOM' and valor != 0"
    )
    fun areDiasInicialesInitialised(year: Int): Flow<Int>

    @Query("Select * from tabla_dias_iniciales where anio = :year and tipo = 'DCOM'")
    fun getDCOMsIniciales(year: Int): Flow<CuDiasIniciales?>

    @Query("Delete from tabla_dias_iniciales")
    suspend fun deleteAllDiasIniciales()


    ////////////////////////CU_historial
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCuHistorial(cuHistorial: CuHistorial)

    @Query("Select * from tabla_historial where idDetalle = :id order by updated desc")
    fun getHistorialDeUnTurno(id: Long?): Flow<List<CuHistorial>>

    @Query("Select * from tabla_historial")
    fun getAllHistorial(): Flow<List<CuHistorial>>

    @Query("Delete from tabla_historial")
    suspend fun deleteAllHistorial()

    //////////////////////// TurnoCompi
    @Query("Select * from tabla_turno_compi")
    fun getAllTurnosCompis(): Flow<List<TurnoCompi>>

    @Query(
        "Select * from tabla_turno_compi " +
                "left join LsUsers on tabla_turno_compi.idUsuario = LsUsers.id " +
                "where " +
                "fecha between :fechaInicial AND :fechaFinal " +
                "order by surname, name, fecha"
    )
    fun getAllTurnosCompisOfMonth(fechaInicial: Long?, fechaFinal: Long?): Flow<List<TurnoCompi>>

    @Query(
        "Select distinct name || ' ' || surname from tabla_turno_compi " +
                "left join LsUsers on tabla_turno_compi.idUsuario = LsUsers.id " +
                "where " +
                "fecha between :fechaInicial AND :fechaFinal " +
                "order by surname, name, fecha"
    )
    fun getCompisWithTC(fechaInicial: Long?, fechaFinal: Long?): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTurnoCompi(turnoCompi: TurnoCompi)

    @Query("Delete From tabla_turno_compi")
    suspend fun deleteAllTurnosCompis()

    @Query("Delete From tabla_turno_compi where idUsuario = :id")
    suspend fun deleteTurnosCompisOfUser(id: Long?)


    ////////////////////////Usuarios

    @Query("Select * from lsusers where id = :id")
    fun getCompi(id: Long): Flow<LsUsers?>


    @Query(
        "Select username, name, surname, " +
                "case when mostrarTelfPersonal = '1' then personalPhone else '' end personalPhone, " +
                "case when mostrarTelfTrabajo = '1' then workPhone else '' end workPhone, " +
                "case when mostrarTelfTrabajo = '1' then workPhoneExt else '' end workPhoneExt, " +
                "mostrarTelfPersonal,mostrarTelfTrabajo " +
                "FROM LsUsers " +
                "where admin = '1' " +
                "order by surname, name desc"
    )
    fun getTelefonosDeAdmins(): Flow<List<TelefonosUsuario>>

    @Query(
        "Select username, name, surname, " +
                "case when mostrarTelfPersonal = '1' then personalPhone else '' end personalPhone, " +
                "case when mostrarTelfTrabajo = '1' then workPhone else '' end workPhone, " +
                "case when mostrarTelfTrabajo = '1' then workPhoneExt else '' end workPhoneExt, " +
                "mostrarTelfPersonal,mostrarTelfTrabajo " +
                "FROM LsUsers " +
                "where mostrarTelfTrabajo = '1' OR mostrarTelfPersonal = '1' " +
                "order by surname, name desc"
    )
    fun getTelefonosDeUsuarios(): Flow<List<TelefonosUsuario>>

    @Query(
        "Select username, name, surname, " +
                "case when mostrarTelfPersonal = '1' then personalPhone else '' end personalPhone, " +
                "case when mostrarTelfTrabajo = '1' then workPhone else '' end workPhone, " +
                "case when mostrarTelfTrabajo = '1' then workPhoneExt else '' end workPhoneExt, " +
                "mostrarTelfPersonal,mostrarTelfTrabajo " +
                "FROM LsUsers " +
                "where (mostrarTelfTrabajo = '1' OR mostrarTelfPersonal = '1') AND " +
                "(  :text = 'todo' " +
                "   OR (normalizedName || ' ' || normalizedSurname) LIKE '%' || :text || '%' " +
                "   OR (personalPhone LIKE '%' || :text || '%' AND mostrarTelfPersonal = '1') " +
                "   OR (workPhoneExt LIKE '%' || :text || '%' AND mostrarTelfTrabajo = '1') " +
                "   OR (workPhone LIKE '%' || :text || '%' AND mostrarTelfTrabajo = '1') ) " +
                "order by surname, name desc"
    )
    fun getTelefonosDeUsuariosBuscados(text: String): Flow<List<TelefonosUsuario>>

    @Query(
        "Select case " +
                "when mostrarCuadros = '1' then 1 else 0 " +
                "end result " +
                "from LsUsers where id = :myUserId"
    )
    fun getMyUserPermisoTurnos(myUserId: String?): Flow<Int>

    @Query(
        "Select username, name, surname, " +
                "case when mostrarTelfPersonal = '1' then personalPhone else '' end personalPhone, " +
                "case when mostrarTelfTrabajo = '1' then workPhone else '' end workPhone, " +
                "case when mostrarTelfTrabajo = '1' then workPhoneExt else '' end workPhoneExt, " +
                "mostrarTelfPersonal,mostrarTelfTrabajo " +
                "FROM LsUsers " +
                "where id = :id " +
                "order by surname, name desc"
    )
    fun getTelefonoDeUnUsuario(id: Long): Flow<TelefonosUsuario>

    @Query("Select name || ' ' || surname from lsusers where id = :id")
    fun getNameOfUsuario(id: Long): Flow<String?>

    @Query("Select * from LsUsers where id = :id ")
    fun getUsuario(id: Long?): Flow<LsUsers?>

    @Query(
        "Select * from LsUsers where cambiosActivados = '1' " +
                "order by surname asc, name asc"
    )
    fun getUsuariosWithCambios(): Flow<List<LsUsers>>

    @Query(
        "Select * from LsUsers " +
                "where cambiosActivados = '1' and id != :myId " +
                "order by surname asc, name asc"
    )
    fun getUsuariosWithCambiosButMe(myId: Long): Flow<List<LsUsers>>

    @Query(
        "Select U.id, U.username, U.email, U.name, U.surname, " +
                "U.mostrarTelfTrabajo, U.mostrarTelfPersonal, " +
                "U.cambiosActivados, U.cambiosActivadosCuando, U.recibirEmailNotificaciones, " +
                "U.mostrarCuadros, U.mostrarCuadrosCuando " +
                "from  LsUsers as U " +
                "where id = :myUserId"
    )
    fun getConfigUsuario(myUserId: String?): Flow<UserConfig?>

    @Query("Select exists (Select * from LsUsers limit 1)")
    fun tenemosUsuarios(): Flow<Boolean>

    @Query("Select name from LsUsers where id = :id")
    fun getMyKiritoUserName(id: Long): Flow<String?>

    @Query("Select * from LsUsers where id = :id")
    fun getMyKiritoUser(id: Long): Flow<LsUsers>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuarios(usuario: LsUsers)

    @Query("Delete From LsUsers")
    suspend fun deleteAllUsuarios()

    /////////////////////////ColoresHoraTurnos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColoresHoraTurnos(coloresHoraTurnos: ColoresHoraTurnos)

    @Query(
        "Select * from tabla_colores_hora_turnos " +
                "order by horaInicio asc"
    )
    fun getAllColoresHoraTurnos(): Flow<List<ColoresHoraTurnos>>

    @Query(
        "Select * from tabla_colores_hora_turnos " +
                "order by id desc limit 1"
    )
    fun getUltimoColorHoraTurnos(): Flow<ColoresHoraTurnos>

    @Query(
        "update tabla_colores_hora_turnos set color = :color, horaInicio = :horaInicio " +
                "where id = :id"
    )
    suspend fun updateColoresHoraTurnos(id: Long, color: Int, horaInicio: Long)

    @Query("Delete from tabla_colores_hora_turnos where id = :id")
    suspend fun deleteColoresHoraTurnos(id: Long)

    //////////////////////////UPDATED_TABLES
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInUpdatedTables(updatedTables: UpdatedTables)

    @Query(
        "select updated from tabla_updated_tables " +
                "where tableName = :tableName AND year = :year"
    )
    fun getTableUpdateOfYear(tableName: String, year: Int): Flow<Long?>

    @Query(
        "select updated from tabla_updated_tables " +
                "where tableName = :tableName AND year = 0"
    )
    fun getTableUpdateWOYear(tableName: String): Flow<Long?>

    @Query(
        "select updated from tabla_updated_tables " +
                "where tableName = :tableName AND year = 0"
    )
    fun getFlowTableUpdateWOYear(tableName: String): Flow<Long?>

    @Query(
        "Select * from tabla_updated_tables " +
                "where tableName = :tableName"
    )
    fun getTableUpdates(tableName: String): Flow<List<UpdatedTables>>

    @Query(
        "Select year from tabla_updated_tables " +
                "where tableName = :tableName"
    )
    fun getTableUpdatedYears(tableName: String): Flow<List<Int>>


    ///////////////////////////CONFIGURACION_APK

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setLogoutFlag(configuracionApk: ConfiguracionAPK)

    @Query(
        "Select valorConfiguracion from tabla_configuracion_apk " +
                "where nombreConfiguracion = 'flag_logout' "
    )
    fun checkLogoutFlag(): Flow<Int?>

    @Query(
        "select valorConfiguracion from tabla_configuracion_apk " +
                "where nombreConfiguracion = 'configTiempoEntreTurnos'"
    )
    fun getConfigTiempoEntreTurnos(): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInConfiguracion_APK(configuracionApk: ConfiguracionAPK)

    @Upsert
    suspend fun upsertConfiguracionAPK(configuracionApk: ConfiguracionAPK)

    @Update
    suspend fun updateConfiguracion_APK(configuracionApk: ConfiguracionAPK)

    //Usamos Flow para que lo que haya en la tabla esté siempre actualizado.
    @Query("SELECT * FROM tabla_configuracion_apk")
    fun getAllFromConfiguracion_APK(): Flow<List<ConfiguracionAPK>>

    @Query("SELECT * FROM tabla_configuracion_apk WHERE nombreConfiguracion = :key limit 1")
    fun getConfiguracionAPK(key: String): Flow<ConfiguracionAPK?>


    @Query("DELETE FROM tabla_configuracion_apk")
    suspend fun clearConfiguracion_APK()


    //////////////////// CaPeticiones
    @Query("Select * from tabla_peticiones_ca where estado = :estado")
    fun getCaPeticionesConEstado(estado: String): Flow<List<CaPeticiones>>

    @Query("Select * from tabla_peticiones_ca where fecha = :fecha")
    fun getCaPeticionesDeDia(fecha: Long): Flow<CaPeticiones?>

    @Query("Select * from tabla_peticiones_ca where id = :id")
    fun getCaPeticionDeId(id: Long): Flow<CaPeticiones?>

    @Query("Select EXISTS(Select * from LsUsers where cambiosActivados = '1' and id = :id)")
    fun tengoLosCambiosActivados(id: Long): Flow<Boolean>

    @Query(
        "Select count(*) from tabla_peticiones_ca " +
                "where estado = 'nuevo' and idUsuarioRecibe = :id"
    )
    fun cambiosNuevos(id: Long): Flow<Int>

    @Query(
        "Select EXISTS(Select * from tabla_peticiones_ca " +
                "where fecha = :fecha and id != :idPeticion and " +
                "(estado = 'pendiente' or estado = 'nuevo' or estado = 'oculto') )"
    )
    fun cambiosPendientesUnDia(fecha: Long, idPeticion: Long): Flow<Boolean>

    @Query(
        "Select EXISTS(Select cambiosActivados from LsUsers " +
                "where cambiosActivados = '1' and :miId = id)"
    )
    fun compiEnEnSistemaCambios(miId: Long): Flow<Boolean>

    @Query(
        "select * from LsUsers where " +
                "(id in (select pc1.idUsuarioPide from tabla_peticiones_ca as pc1) OR " +
                "id in (select pc2.idUsuarioRecibe from tabla_peticiones_ca as pc2)) AND " +
                "id != :miId "
    )
    fun getCompisCambiadosConSisCamb(miId: Long): Flow<List<LsUsers>>

    @Query(
        "Select EXISTS(Select * from tabla_peticiones_ca " +
                "where  id == :idPeticion) "
    )
    fun existeIdPeticionCambio(idPeticion: Long?): Flow<Boolean>


    //idMiUser aquí sale como -1, luego se modifica con el id del usuario, que está en preferencias.
    @Query(
        "select pc.id, -1 as idMiUser, pc.idUsuarioPide, pc.idUsuarioRecibe, " +
                "u1.name || ' ' || u1.surname as nameUsuarioPide, " +
                "u2.name || ' ' || u2.surname as nameUsuarioRecibe, " +
                "pc.turnoUsuarioPide as turnoUsuarioPide, pc.turnoUsuarioRecibe as turnoUsuarioRecibe, " +
                "tc.turno as turnoCompiPide, tc2.turno as turnoCompiRecibe, " +
                "cd.turno as miTurnoCD, " +
                "pc.fecha, pc.dtPeticion, pc.dtRespuesta, pc.estado " +
                "from tabla_peticiones_ca as pc " +
                "left join lsusers as u1 on pc.idUsuarioPide = u1.id " +
                "left join lsusers as u2 on pc.idUsuarioRecibe = u2.id " +
                "left join tabla_turno_compi as tc on pc.fecha = tc.fecha and pc.idUsuarioPide = tc.idUsuario " +
                "left join tabla_turno_compi as tc2 on pc.fecha = tc2.fecha and pc.idUsuarioRecibe = tc2.idUsuario " +
                "left join tabla_cu_detalle as cd on pc.fecha = cd.fecha " +
                "where :filter = 0 or :filter = 1 and (pc.estado = 'pendiente' or pc.estado = 'nuevo' or " +
                "pc.estado = 'oculto' and pc.idUsuarioRecibe = :miId) " +
                "order by pc.fecha desc, pc.dtPeticion desc "
    )
    fun getAllCaPeticiones(filter: Int, miId: Long): Flow<List<CaPeticionesDomain>>

    @Query(
        "select pc.id, -1 as idMiUser, pc.idUsuarioPide, pc.idUsuarioRecibe, " +
                "u1.name || ' ' || u1.surname as nameUsuarioPide, " +
                "u2.name || ' ' || u2.surname as nameUsuarioRecibe, " +
                "pc.turnoUsuarioPide as turnoUsuarioPide, pc.turnoUsuarioRecibe as turnoUsuarioRecibe, " +
                "tc.turno as turnoCompiPide, tc2.turno as turnoCompiRecibe, " +
                "cd.turno as miTurnoCD, " +
                "pc.fecha, pc.dtPeticion, pc.dtRespuesta, pc.estado " +
                "from tabla_peticiones_ca as pc " +
                "left join lsusers as u1 on pc.idUsuarioPide = u1.id " +
                "left join lsusers as u2 on pc.idUsuarioRecibe = u2.id " +
                "left join tabla_turno_compi as tc on pc.fecha = tc.fecha and pc.idUsuarioPide = tc.idUsuario " +
                "left join tabla_turno_compi as tc2 on pc.fecha = tc2.fecha and pc.idUsuarioRecibe = tc2.idUsuario " +
                "left join tabla_cu_detalle as cd on pc.fecha = cd.fecha " +
                "where (:fPendiente = 1 and (pc.estado = 'pendiente' or pc.estado = 'nuevo' or pc.estado = 'oculto' and pc.idUsuarioPide = :miId) OR " +
                ":fOculto = 1 and pc.estado = 'oculto' and pc.idUsuarioRecibe = :miId OR " +
                ":fAceptado = 1 and pc.estado = 'aceptado'  OR " +
                ":fCancelado = 1 and (pc.estado = 'cancelado' or pc.estado = 'rechazado')) AND " +
                //Ahora parte del compi y enviado por:
                "(" +
                "((:fCompi = pc.idUsuarioPide OR :fCompi = pc.idUsuarioRecibe OR :fCompi = -1) AND :fEnviadoPor = 0) OR " +//Enviado por todos.
                "(:miId = pc.idUsuarioPide AND :fEnviadoPor = 1) OR " +//Enviados por mí.
                "(:miId = pc.idUsuarioRecibe AND :fEnviadoPor = 2)  " +//Enviados por el otro.
                ") " +
                "order by case " +
                "   when :fOrden = 0 THEN pc.fecha " +
                "   when :fOrden = 1 THEN pc.dtPeticion " +
                "   when :fOrden = 2 THEN pc.dtRespuesta " +
                "   when :fOrden = 3 AND :fEnviadoPor = 1 THEN u2.surname  " +
                "   when :fOrden = 3 AND :fEnviadoPor = 2 THEN u1.surname  " +
                "   when :fOrden = 3 AND :miId = u1.id THEN u2.surname  " +
                "   when :fOrden = 3 AND :miId = u2.id THEN u1.surname  " +
                "   else pc.estado " +
                "end asc "
    )
    fun getFilteredCaPeticiones(
        miId: Long,
        fPendiente: Int,
        fOculto: Int,
        fAceptado: Int,
        fCancelado: Int,
        fOrden: Int,
        fCompi: Long,
        fEnviadoPor: Int
    ): Flow<List<CaPeticionesDomain>>

    @Upsert
    suspend fun upsertCaPeticiones(caPeticiones: CaPeticiones)

    @Query("delete from tabla_peticiones_ca where id = :id")
    suspend fun deleteCaPeticiones(id: Long)

    /////////////////////////// CLIMA //////////////////////////
    @Query(
        "select * from tabla_clima " +
                "where time = :time and estacion = :station " +
                "limit 1"
    )
    fun getOneClima(time: Long, station: String): Flow<Clima?>

    @Upsert
    suspend fun upsertClima(clima: Clima)

    @Query("delete from tabla_clima where time < :time")
    suspend fun deleteOldClima(time: Long)


    /////////////////////////// LOCALIZADORES //////////////////////////
    @Query(
        "select * from tabla_localizadores " +
                "where fecha = :date and turno = :turno " +
                "limit 1"
    )
    fun getOneLocalizador(date: Long, turno: String): Flow<Localizador?>

    @Upsert
    suspend fun upsertLocalizador(localizador: Localizador)

    @Query("delete from tabla_localizadores where fecha < :date")
    suspend fun deleteOldLocalizador(date: Long)


    /////////////////////////// TABLON ANUNCIOS //////////////////////////

    @Upsert
    suspend fun upsertTablonAnuncio(tablonAnuncios: TablonAnuncios)

    @Query("Delete from tabla_anuncios where id = :id")
    suspend fun deleteAnuncio(id: Long?)

    @Query(
        "Select ta.id, 0 as miId, cd.turno as miTurno, cd.tipo as miTipo, ta.idUsuario, tu.name || ' ' || tu.surname as nombreUsuario, " +
                "tc.turno as turnoUsuario, " +
                "ta.fecha, ta.titulo, ta.explicacion, ta.etiqueta1, ta.etiqueta2, ta.etiqueta3, " +
                "ta.updated " +
                "from tabla_anuncios as ta " +
                "left join lsusers as tu on ta.idUsuario = tu.id " +
                "left join tabla_turno_compi as tc on ta.idUsuario = tc.idUsuario and tc.fecha = ta.fecha " +
                "left join tabla_cu_detalle as cd on cd.fecha = ta.fecha " +
                "order by ta.fecha asc"
    )
    fun getAllTablonAnuncios(): Flow<List<TablonAnunciosItem>>

    @Query(
        "Select ta.id, 0 as miId, ta.idUsuario, tu.name || ' ' || tu.surname as nombreUsuario, " +
                "ta.fecha, ta.titulo, ta.explicacion, ta.etiqueta1, ta.etiqueta2, ta.etiqueta3, " +
                "ta.updated " +
                "from tabla_anuncios as ta " +
                "left join LsUsers as tu on ta.idUsuario = tu.id " +
                "where ta.id = :idAnuncio"
    )
    fun getOneTablonAnuncio(idAnuncio: Long): Flow<TablonAnunciosItem>

    @Query(
        "Select ta.idUsuario, ta.fecha " +
                "from tabla_anuncios as ta " +
                "left join LsUsers as tu on ta.idUsuario = tu.id " +
                "where ta.id = :idAnuncio"
    )
    fun getTablonAnuncioNavParams(idAnuncio: Long): Flow<TablonAnunciosNavigationParams>

    @Query(
        "SELECT etiqueta, COUNT(*) AS count " +
                "FROM ( " +
                "    SELECT etiqueta1 AS etiqueta FROM tabla_anuncios WHERE etiqueta1 IS NOT NULL " +
                "    UNION ALL " +
                "    SELECT etiqueta2 AS etiqueta FROM tabla_anuncios WHERE etiqueta2 IS NOT NULL " +
                "    UNION ALL " +
                "    SELECT etiqueta3 AS etiqueta FROM tabla_anuncios WHERE etiqueta3 IS NOT NULL " +
                ") AS etiquetas " +
                "GROUP BY etiqueta " +
                "ORDER BY count DESC " +
                "limit 10"
    )
    fun getImportantEtiquetas(): Flow<List<EtiquetaDeTablonAnuncios>>

    @Query(
        "SELECT distinct etiqueta " +
                "FROM ( " +
                "    SELECT etiqueta1 AS etiqueta FROM tabla_anuncios WHERE etiqueta1 IS NOT NULL " +
                "    UNION ALL " +
                "    SELECT etiqueta2 AS etiqueta FROM tabla_anuncios WHERE etiqueta2 IS NOT NULL " +
                "    UNION ALL " +
                "    SELECT etiqueta3 AS etiqueta FROM tabla_anuncios WHERE etiqueta3 IS NOT NULL " +
                ") AS etiquetas " +
                "GROUP BY etiqueta "
    )
    fun getAllEtiquetas(): Flow<List<String>>

    @Query("delete from tabla_anuncios where fecha < :hoy")
    suspend fun deleteOldTablonAnuncios(hoy: Long)

    ////// TELEFONOS EMPRESA
    @Query("SELECT * FROM tabla_telefonos_importantes")
    fun getTelefonosDeEmpresa(): Flow<List<TelefonoImportante>>

    @Query("DELETE FROM tabla_telefonos_importantes WHERE id = :id")
    suspend fun deleteTelefonoDeEmpresa(id: Long)

    @Upsert
    suspend fun upsertTelefonoDeEmpresa(telefonoImportante: TelefonoImportante)
}

