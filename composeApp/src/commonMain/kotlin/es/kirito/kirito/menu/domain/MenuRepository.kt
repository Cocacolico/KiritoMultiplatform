package es.kirito.kirito.menu.domain

import es.kirito.kirito.core.data.dataStore.preferenciasKirito
import es.kirito.kirito.core.data.database.GrGraficos
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.core.data.database.LsUsers
import es.kirito.kirito.core.data.network.KiritoRequest
import es.kirito.kirito.core.data.network.models.ResponseUserDTO
import es.kirito.kirito.core.domain.asDatabaseModel
import es.kirito.kirito.core.domain.kiritoError.lanzarExcepcion
import es.kirito.kirito.core.domain.util.fromDateTimeStringToLong
import es.kirito.kirito.core.domain.util.normalizeAndRemoveAccents
import es.kirito.kirito.menu.data.network.models.RequestEditarMiUsuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MenuRepository: KoinComponent {
    private val database: KiritoDatabase by inject()
    private val dao = database.kiritoDao()
    private val ktor = KiritoRequest()
    private val preferences = preferenciasKirito

    val cambiosNuevos = flow {
        emit(dao.cambiosNuevos(preferences.map { it.userId }.first()).first())
    }
    val mensajesAdminNuevos = dao.mensajesAdminNuevos()

    fun getNumLzs(fechaInicial: Long?, fechaFinal: Long?, year: Int) =
        dao.getNumLzs(fechaInicial, fechaFinal, year)
    fun getNumLzas(fechaInicial: Long?, fechaFinal: Long?, year: Int) =
        dao.getNumLzas(fechaInicial, fechaFinal, year)
    fun getNumComjs(fechaInicial: Long?, fechaFinal: Long?, year: Int) =
        dao.getNumComjs(fechaInicial, fechaFinal, year)

    fun getNumDds(fechaInicial: Long?, fechaFinal: Long?, year: Int) =
        dao.getNumDds(fechaInicial, fechaFinal, year)

    fun getNumDjs(fechaInicial: Long?, fechaFinal: Long?, year: Int) =
        dao.getNumDjs(fechaInicial, fechaFinal, year)

    fun getNumDjas(fechaInicial: Long?, fechaFinal: Long?, year: Int) =
        dao.getNumDjas(fechaInicial, fechaFinal, year)

    fun getNumLibras(fechaInicial: Long?, fechaFinal: Long?, year: Int) =
        dao.getNumLibras(fechaInicial, fechaFinal, year)

    fun getGraficosDeEnCatorceDias(fechaElegida: Long?): Flow<List<GrGraficos>> {
        return dao.getGraficosDeEnCatorceDias(fechaElegida)
    }
    fun getGraficoDeUnDia(fechaElegida: Long?): Flow<GrGraficos?> {
        return dao.getGraficoDeUnDia(fechaElegida)
    }

    fun areDiasInicialesInitialised(year: Int) =
        dao.areDiasInicialesInitialised(year)
    fun checkLogoutFlag(): Flow<Int> {
        return dao.checkLogoutFlag()
    }

    suspend fun updateMyUserData(datosUsuario: RequestEditarMiUsuario) {
        val respuesta = ktor.requestUpdateMyUser(datosUsuario)
        respuesta.error.lanzarExcepcion()
        if(respuesta.respuesta != null) {
            dao.insertUsuarios(respuesta.respuesta.asDatabaseModel())
        }
    }
}