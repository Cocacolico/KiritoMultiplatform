package es.kirito.kirito.precarga.domain

import es.kirito.kirito.core.data.dataStore.preferenciasKirito
import es.kirito.kirito.core.data.dataStore.updatePreferenciasKirito
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.core.data.database.OtFestivo
import es.kirito.kirito.core.data.network.KiritoRequest
import es.kirito.kirito.core.data.network.models.RequestUpdatedDTO
import es.kirito.kirito.core.domain.CoreRepository
import es.kirito.kirito.core.domain.util.enFormatoDeSalida
import es.kirito.kirito.core.domain.util.fromDateStringToLong
import es.kirito.kirito.core.domain.util.toInstant
import es.kirito.kirito.precarga.data.network.models.ResponseOtFestivosDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class PrecargaRepository() : KoinComponent {
    private val database: KiritoDatabase by inject()
    private val coreRepo: CoreRepository by inject()
    private val dao = database.kiritoDao()
    private val ktor = KiritoRequest()


    val festivos = dao.getOtFestivos()



    suspend fun updateKiritoDatabase() {


        //TODO: Desde aquí: Cuando ya sea la 2a y siguientes veces, hay que hacer esto desde un work
        // y desde el equivalente de iOS.
        //En ambos casos hay que hacer un callback o similar para mostrar la sincronización en la barra inferior.
        val bdActualizada = coreRepo.getUpdatedDB().toInstant()
        if (bdActualizada.epochSeconds == 0L) {
            //TODO: Ordenar que se muestre el trenecito de la screen.
            setInstallDT()
            //TODO: Clear all tables.
            downloadAndRefreshDB(bdActualizada)//Primera vez, bajamos en primer plano.

        } else
            downloadAndRefreshDB(bdActualizada)//Esta deberá ser en un futuro con workManager.

    }

    private suspend fun setInstallDT() {
        val installDT = preferenciasKirito.first().installDT
        if (installDT == -1L)
            updatePreferenciasKirito { settings ->
                settings.copy(
                    installDT = Clock.System.now().epochSeconds
                )
            }
    }

    private suspend fun downloadAndRefreshDB(bdActualizada: Instant) {
        //TODO: Alarmas.
        //TODO: Solo hacer esto si hay internet. Devolver error si no.

        refreshOtFestivos(bdActualizada)


    }


    private suspend fun refreshOtFestivos(bdActualizada: Instant) {

        val salida =
            RequestUpdatedDTO(
                "festivos.obtener",
                if (bdActualizada.epochSeconds != 0L)
                    bdActualizada.enFormatoDeSalida()
                else null
            )
        val respuesta = ktor.requestOtFestivos(salida)

        if (respuesta.error.errorCode == "0" && respuesta.respuesta != null) {
            respuesta.respuesta.forEach {
                dao.insertOtFestivos(it.asDatabaseModel())
            }
        }


    }
}

private fun ResponseOtFestivosDTO.asDatabaseModel(): OtFestivo {
    val formattedFecha = this.fecha.fromDateStringToLong()
    val cosa = OtFestivo(
        fecha = formattedFecha,
        idFestivo = this.idFestivo?.toLong() ?: 0,
        descripcion = this.descripcion
    )
    println("La coosa vale $cosa")

    return cosa
}



