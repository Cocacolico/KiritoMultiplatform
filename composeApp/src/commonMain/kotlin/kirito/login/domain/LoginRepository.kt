package kirito.login.domain

import es.kirito.kirito.login.data.network.ResponseResidenciasDTO
import kirito.core.data.network.KiritoRequest
import kirito.core.data.network.ResponseKiritoDTO

class LoginRepository {
    private val ktor =  KiritoRequest()

    suspend fun getResidencias(): ResponseKiritoDTO<ResponseResidenciasDTO> {
       return ktor.getResidencias()
    }

}