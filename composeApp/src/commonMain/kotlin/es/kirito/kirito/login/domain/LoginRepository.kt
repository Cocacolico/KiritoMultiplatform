package es.kirito.kirito.login.domain

import es.kirito.kirito.core.data.database.Estaciones
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.core.data.network.KiritoRequest
import es.kirito.kirito.core.data.network.ResponseKiritoDTO
import es.kirito.kirito.core.data.utils.KiritoException
import es.kirito.kirito.core.data.utils.KiritoUserBlockedException
import es.kirito.kirito.login.data.network.ResponseLoginDTO
import es.kirito.kirito.login.data.network.ResponseRegisterUserDTO
import es.kirito.kirito.login.data.network.ResponseResidenciasDTO
import es.kirito.kirito.login.data.network.ResponseRespuestaOtEstaciones
import kotlinx.coroutines.flow.first

class LoginRepository(
    private val database: KiritoDatabase
) {
    private val ktor =  KiritoRequest()
    suspend fun getResidencias(): ResponseKiritoDTO<ResponseResidenciasDTO> {
       return ktor.getResidencias()
    }
    suspend fun registerNewUser(
        residenciaSeleccionada: String,
        datosUsuario: RegisterData,
        tokenFCM: String
    ): ResponseKiritoDTO<ResponseRegisterUserDTO> {
        return ktor.requestRegistro(residenciaSeleccionada,datosUsuario,tokenFCM)
    }

    suspend fun getMyKiritoToken(
        usuario: String,
        password: String,
        nombreDispositivo: String,
        tokenFCM: String,
    ): ResponseKiritoDTO<ResponseLoginDTO> {
        val respuesta = ktor.requestLogin(usuario, password, nombreDispositivo, tokenFCM)
        return if (respuesta.error.errorCode == "0") { // Login exitoso
            val tiempoBloqueado = respuesta.respuesta?.respuesta?.seconds
            if (tiempoBloqueado != null) { // Comprobamos que el usuario no esté bloqueado
                throw KiritoUserBlockedException("Bloqueado durante $tiempoBloqueado segundos.")
            }
            respuesta
        } else {
            when(respuesta.error.errorCode) {
                "13" -> respuesta // Usuario pendiente de autorizar
                else -> throw KiritoException("Error: ${respuesta.error}")
            }
        }
    }
    // Como da error la API al no haber negociado antes el login,
    // para probar si funciona bien la BBDD retornamos un Boolean y lo tratamos en el viewModel
    suspend fun refreshEstaciones(): Boolean {
        val hayEstaciones = database.kiritoDao().hayEstaciones().first()
        return if(hayEstaciones)
            true
        else
            false
        /*if (!hayEstaciones) {//Solo descargamos si no hay estaciones.
            val respuesta = ktor.requestOtEstaciones()
            if (respuesta.error.errorCode == "0") {
                respuesta.respuesta?.respuesta?.forEach {
                    database.kiritoDao().insertEstacion(it.asDBModel())
                }
                println("Estaciones en la BBDD:")
                database.kiritoDao().getAllEstaciones().map {
                    for (estacion in it) {
                        println(estacion.nombre)
                }
                }
            } else
                throw KiritoException("Error: ${respuesta.error}")
        }*/
    }

    private fun ResponseRespuestaOtEstaciones.asDBModel(): Estaciones {
        return Estaciones(
            nombre = nombre,
            acronimo = acronimo,
            numero = numero,
            longitud = null,
            latitud = null,
        )
    }

    /* suspend fun registerNewUser(
         residenciaSeleccionada: String,
         urlSeleccionada: String,
         userToBeRegistered: UserToBeRegistered,
         tokenFCM: String
     ): Boolean {
         return withContext(Dispatchers.IO) {
             if (respuesta.body()?.respuesta?.PuedeAccederInmediatamente == true) {
                 //En este caso, guardamos los valores que tenemos en nuestras preferencias y entramos.
                 val nombreDispositivo = android.os.Build.MODEL

                 val miToken = getMyKiritoToken(
                     usuario = userToBeRegistered.username,
                     password = userToBeRegistered.password,
                     nombreDispositivo = nombreDispositivo,
                     tokenFCM = tokenFCM
                 )
                 if (!miToken?.respuesta?.login?.token.isNullOrBlank()) {
                     //Tenemos un token válido, entonces:
                     dataStore.updateData {
                         Timber.i("nombreDataStore repo1 ${miToken?.respuesta?.login?.id_usuario}")
                         it.copy(
                             matricula = userToBeRegistered.username,
                             userId = miToken?.respuesta?.login?.id_usuario?.toLong() ?: -1L,
                             token = miToken!!.respuesta!!.login.token,
                             residenciaURL = urlSeleccionada,
                             residenciaName = residenciaSeleccionada
                         )
                     }
                     return@withContext true
                 }
             }
             return@withContext false
         }
     }*/

}