package es.kirito.kirito.core.domain

import es.kirito.kirito.core.data.constants.MyConstants
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.core.data.network.KiritoRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CoreRepository: KoinComponent {
    private val database: KiritoDatabase by inject()
    private val dao = database.kiritoDao()
    private val ktor = KiritoRequest()

   suspend fun getUpdatedDB() = dao.getTableUpdateWOYear(MyConstants.GENERAL_UPLOAD)





}