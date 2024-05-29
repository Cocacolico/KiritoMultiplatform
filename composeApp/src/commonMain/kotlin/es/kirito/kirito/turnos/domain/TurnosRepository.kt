package es.kirito.kirito.turnos.domain

import es.kirito.kirito.core.data.constants.MyConstants
import es.kirito.kirito.core.data.database.Clima
import es.kirito.kirito.core.data.database.CuHistorial
import es.kirito.kirito.core.data.database.GrGraficos
import es.kirito.kirito.core.data.database.GrNotasTurno
import es.kirito.kirito.core.data.database.GrTareas
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.core.data.database.OtColoresTrenes
import es.kirito.kirito.core.data.database.OtTeleindicadores
import es.kirito.kirito.core.data.network.KiritoRequest
import es.kirito.kirito.core.domain.models.CuDetalleConFestivoDBModel
import es.kirito.kirito.core.domain.models.GrTarea
import es.kirito.kirito.core.domain.models.TurnoPrxTr
import es.kirito.kirito.core.domain.util.roundUpToHour
import es.kirito.kirito.core.domain.util.toInstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TurnosRepository: KoinComponent {

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
    ): Flow<List<GrTarea>> {
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

    fun checkLogoutFlag(): Flow<Int> {
        return dao.checkLogoutFlag()
    }





}