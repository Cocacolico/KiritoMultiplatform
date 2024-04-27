package es.kirito.kirito.login.domain

import es.kirito.kirito.login.data.network.ResponseResidenciasDTO
import es.kirito.kirito.core.data.network.KiritoRequest
import es.kirito.kirito.core.data.network.ResponseKiritoDTO

class LoginRepository {
    private val ktor =  KiritoRequest()

    suspend fun getResidencias(): ResponseKiritoDTO<ResponseResidenciasDTO> {
       return ktor.getResidencias()
    }

}