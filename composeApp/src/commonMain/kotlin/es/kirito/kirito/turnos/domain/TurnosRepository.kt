package es.kirito.kirito.turnos.domain

import es.kirito.kirito.core.data.database.Clima
import es.kirito.kirito.core.data.database.CuDetalle
import es.kirito.kirito.core.data.database.CuHistorial
import es.kirito.kirito.core.data.database.GrGraficos
import es.kirito.kirito.core.data.database.GrNotasTurno
import es.kirito.kirito.core.data.database.GrTareas
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.core.data.database.OtColoresTrenes
import es.kirito.kirito.core.data.database.OtTeleindicadores
import es.kirito.kirito.core.data.network.KiritoRequest
import es.kirito.kirito.core.domain.backgroundWorks.enqueueEditShiftBackgroundWork
import es.kirito.kirito.core.domain.kiritoError.lanzarExcepcion
import es.kirito.kirito.core.domain.models.CuDetalleConFestivoDBModel
import es.kirito.kirito.core.domain.models.GrTareaBuscador
import es.kirito.kirito.core.domain.models.GrTareaConClima
import es.kirito.kirito.core.domain.models.TurnoBuscador
import es.kirito.kirito.core.domain.models.TurnoPrxTr
import es.kirito.kirito.core.domain.util.esTipoValido
import es.kirito.kirito.core.domain.util.esTurnoConNumero
import es.kirito.kirito.core.domain.util.esTurnoConNumeroONumero
import es.kirito.kirito.core.domain.util.roundUpToHour
import es.kirito.kirito.core.domain.util.toInstant
import es.kirito.kirito.core.domain.util.toLocalDate
import es.kirito.kirito.turnos.data.network.models.RequestSubirCuadroVacioDTO
import es.kirito.kirito.turnos.domain.models.CuDetalleConFestivoSemanal
import es.kirito.kirito.turnos.domain.models.CuadroAnualVacio
import es.kirito.kirito.turnos.domain.models.OrdenBusqueda
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TurnosRepository : KoinComponent {

    private val database: KiritoDatabase by inject()
    private val dao = database.kiritoDao()
    private val ktor = KiritoRequest()


    fun getCuDetallesDeUnDia(fecha: Int?): Flow<CuDetalleConFestivoDBModel?> {
        return dao.getCuDetallesDeUnDia(fecha)
    }

    fun getTurnoDeUnDia(fecha: Int?): Flow<TurnoPrxTr?> {
        return dao.getTurnoDeUnDia(fecha)
    }

    fun getAllColoresTrenes(): Flow<List<OtColoresTrenes>> {
        return dao.getAllColoresTrenes()
    }

    fun getFestivoDeUnDia(fecha: Int?): Flow<String?> {
        return dao.getFestivoDeUnDia(fecha)
    }

    fun getTareasCortasDeUnTurno(
        idGrafico: Long?,
        turno: String?,
        weekDay: String?
    ): Flow<List<GrTareas>> {
        return dao.getTareasCortasDeUnTurno(idGrafico, turno, weekDay)
    }

    fun getTareasDeUnTurnoDM(
        idGrafico: Long?,
        turno: String?,
        weekDay: String?
    ): Flow<List<GrTareaConClima>> {
        return dao.getTareasDeUnTurnoDM(idGrafico, turno, weekDay)
    }

    fun getOneClima(time: Long, station: String) = dao.getOneClima(time, station)
    fun getOneClimaRounded(time: Long, station: String): Flow<Clima?> {
        val rounded = time.toInstant().roundUpToHour().epochSeconds
        return dao.getOneClima(rounded, station)
    }

    fun getTeleindicadores(
        idGrafico: Long?,
        turno: String?,
        diaSemana: String?
    ): Flow<List<OtTeleindicadores>> {
        return dao.getTeleindicadores(idGrafico, turno, diaSemana)
    }

    fun getNotasDelTurno(
        idGrafico: Long?,
        turno: String?,
        diaSemana: String?
    ): Flow<List<GrNotasTurno>> {
        return dao.getNotasDelTurno(idGrafico, turno, diaSemana)
    }

    val configTiempoEntreTurnos = dao.getConfigTiempoEntreTurnos().map { it ?: 1 }

    fun getHistorialDeUnTurno(
        idDetalle: Long?
    ): Flow<List<CuHistorial>> {
        return dao.getHistorialDeUnTurno(idDetalle)
    }

    fun tengoLosCambiosActivados(id: Long): Flow<Boolean> {
        return dao.tengoLosCambiosActivados(id)
    }

    fun getGraficoDeUnDia(fecha: Long?): Flow<GrGraficos?> {
        return dao.getGraficoDeUnDia(fecha)
    }

    fun getTareasDeUnTurno(
        idGrafico: Long?,
        turno: String?,
    ): Flow<Boolean> {
        return dao.getTareasDeUnTurno(idGrafico, turno)
    }

    fun elTurnoTieneExcelIF(turno: String?, idGrafico: Long?) =
        dao.elTurnoTieneExcelIF(turno, idGrafico)

    fun getOneLocalizador(date: Long, turno: String) =
        dao.getOneLocalizador(date, turno)

    fun checkLogoutFlag(): Flow<Int?> {
        return dao.checkLogoutFlag()
    }

    suspend fun getIdGraficoDeUnDia(fecha: Long): Flow<Long?> {
        return dao.getIdGraficoDeUnDia(fecha)
    }

    fun getTurnoDeEquivalencia(
        equivalencia: String,
        idGrafico: Long?,
        diaSemana: String?
    ) = dao.getTurnoDeEquivalencia(equivalencia, idGrafico, diaSemana)


    suspend fun requestSubirCuadroVacio(cuadroAnualVacio: CuadroAnualVacio): Boolean {
        RequestSubirCuadroVacioDTO(
            peticion = "cuadros.generar_vacio",
            cuadroAnualVacio.year.toString(),
            if (cuadroAnualVacio.sobrescribir) "1" else "0"
        ).let { salida ->
            val respuesta = ktor.requestSubirCuadroVacio(salida)
            return respuesta.error.lanzarExcepcion()
        }
    }

    fun getOneGrTareasFromGrafico(idGrafico: Long): Flow<GrTareas?> {
        return dao.getOneGrTareasFromGrafico(idGrafico)
    }


    fun fechaTieneExcelIF(fecha: Long?) = dao.fechaTieneExcelIF(fecha)

    fun getCuDetallesConFestivos(
        fechaInicial: Long?,
        fechaFinal: Long?
    ): Flow<List<CuDetalleConFestivoSemanal>> =
        dao.getCuDetallesConFestivos(fechaInicial, fechaFinal).map {
            it.asSemanalModel()
        }

    fun getTurnosEntreFechas(fechaInicial: Long?, fechaFinal: Long?) =
        dao.getTurnosEntreFechas(fechaInicial, fechaFinal)

    fun hayTeleindicadores() = dao.hayTeleindicadores()

    fun getCuDetalleDeUnDia(date: Long?) = dao.getCuDetalleDeUnDia(date)


    /**
     * Devuelve el turno buscado.
     * @param idGrafico El id del gráfico a mostrar.
     * @param turno Si en turno pones "todo", se reciben todos los turnos.
     * @param diaSemana También puede ir con "todo".
     * @param orden 0 = Turno, 1 = Hora de inicio, 2 = Compañero
     * **/
    fun getTurnoBuscado2(
        idGrafico: Long?,
        turno: String?,
        diaSemana: String?,
        orden: OrdenBusqueda
    ): Flow<List<TurnoBuscador>> = dao.getTurnoBuscado(
        idGrafico,
        turno,
        diaSemana,
        orden.ordinal,
        Clock.System.todayIn(TimeZone.currentSystemDefault()).toEpochDays().toLong()
    )

    fun getTareasDeUnTurnoBuscador2(
        idGrafico: Long?,
        turno: String?,
        weekDay: String?
    ): Flow<List<GrTareaBuscador>> = dao.getTareasDeUnTurnoBuscador2(idGrafico, turno, weekDay)

    fun getTurnoBuscadoPorTren(
        idGrafico: Long?,
        tren: String?,
        diaSemana: String?
    ): Flow<List<TurnoBuscador>> = dao.getTurnoBuscadoPorTren(idGrafico, tren, diaSemana)

    fun getTeleindicadoresPorTren(tren: String) = dao.getTeleindicadoresPorTren(tren)

    suspend fun editSingleShift(turnoOriginal: CuDetalle?, turnoEditado: CuDetalle, onFinished: ()->Unit) {
        //En primer lugar miramos si hemos modificado el turno con una equivalencia:
        if (turnoOriginal?.turno != turnoEditado.turno) {
            val idGrafico = getIdGraficoDeUnDia(turnoEditado.fecha).first()
            val turnoDesdeEquivalencia =
                getTurnoDeEquivalencia(
                    turnoEditado.turno ?: "",
                    idGrafico,
                    turnoEditado.diaSemana
                ).first()
            println(turnoEditado.toString() + " " + turnoDesdeEquivalencia.toString())
            if (turnoDesdeEquivalencia?.turno != null)
                turnoEditado.turno = turnoDesdeEquivalencia.turno
        }
        //Después, preparamos el tipo y turno para hacerlos equivalentes:
        if (turnoOriginal?.turno == turnoEditado.turno //No hemos modificado el turno
            && turnoOriginal?.tipo != turnoEditado.tipo // pero sí el tipo.
        ) {
            if (!turnoEditado.tipo.esTurnoConNumero()) {
                //Este tipo no es para turnos con número, de serlo, así lo mandamos.
                //Cambiamos el turno a lo mismo que el tipo, para evitar el rebote doble del workManager:
                turnoEditado.turno = turnoEditado.tipo
            }
        } else if (turnoOriginal?.turno != turnoEditado.turno //Hemos modificado el turno
            && turnoOriginal?.tipo == turnoEditado.tipo // y no el tipo.
        ) {
            if (!turnoEditado.turno.esTurnoConNumeroONumero()) {
                //No es un turno con número ni un número, modificamos entonces el tipo también.
                if (turnoEditado.turno.esTipoValido() &&
                    turnoEditado.tipo != "DD"
                )
                    turnoEditado.tipo = turnoEditado.turno ?: ""
            }
            if (turnoEditado.turno?.toIntOrNull() != null &&
                !turnoEditado.tipo.esTurnoConNumero()
            ) {
                //El turno es un número y el tipo NO es de los que acepta números. Lo cambio a T.
                turnoEditado.tipo = "T"//Lo cambio a T.
            }
        }
        println("Pues guardaríamos el turno $turnoEditado")
        enqueueEditShiftBackgroundWork(turnoEditado)
        onFinished()
    }


}


private fun List<CuDetalleConFestivoDBModel>.asSemanalModel(): List<CuDetalleConFestivoSemanal> {
    return map {
        CuDetalleConFestivoSemanal(
            idDetalle = it.idDetalle,
            fecha = it.fecha.toLocalDate(),
            tipo = it.tipo,
            turno = it.turno,
            nombreDebe = it.nombreDebe,
            notas = it.notas,
            idFestivo = it.idFestivo,
            descripcionFestivo = it.descripcion,
            libra = it.libra ?: 0,
            comj = it.comj ?: 0,
            horaInicio = null,
            color = 0,
            excesos = it.excesos,
            mermas = it.mermas,
        )
    }
}