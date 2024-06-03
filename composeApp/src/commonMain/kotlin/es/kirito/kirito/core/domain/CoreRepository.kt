package es.kirito.kirito.core.domain

import es.kirito.kirito.core.data.constants.MyConstants
import es.kirito.kirito.core.data.dataStore.updatePreferenciasKirito
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.core.data.network.KiritoRequest
import es.kirito.kirito.core.data.network.models.RequestSimpleDTO
import es.kirito.kirito.core.domain.kiritoError.lanzarExcepcion
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CoreRepository: KoinComponent {
    private val database: KiritoDatabase by inject()
    private val dao = database.kiritoDao()
    private val ktor = KiritoRequest()

   suspend fun getUpdatedDB() = dao.getTableUpdateWOYear(MyConstants.GENERAL_UPLOAD)


    suspend fun nukeAll() {
        //TODO: Hacer todo lo que es de Android exclusivo.
      //  WorkManager.getInstance(context).cancelAllWork()
      //  ShortcutManagerCompat.removeAllDynamicShortcuts(context)
        logout()
        updatePreferenciasKirito {
            it.copy(
                matricula = "",
                password = "",
                lastAutoDcom = 0,
                autoDateDiasEsp = 0L,
                weatherUpdated = 0L,
                userId = 0L,
                residenciaName = "",
                residenciaURL = "",
                preLogin = false,
                token = "",//Esto va debajo de logout porque se usa el token en la petición.
            )
        }
        //TODO: Implementar el clearAllTables.
       // database.clearAllTables()
    }

    suspend fun logout() {
        try {
           RequestSimpleDTO("usuarios.logout").let { salida ->
              val respuesta = ktor.requestSimpleEmptyResponse(salida)
               respuesta.error.lanzarExcepcion()
           }
        } catch (e: Exception) {
            println(e.message.toString())
        }
    }



}