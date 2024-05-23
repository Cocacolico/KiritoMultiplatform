package es.kirito.kirito.precarga.domain

import androidx.compose.runtime.mutableStateOf
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.core.data.network.KiritoRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class PrecargaRepository(): KoinComponent {
    private val database: KiritoDatabase by inject()
    private val dao = database.kiritoDao()
    private val ktor = KiritoRequest()





    suspend fun updateKiritoDatabase() {

    }


}