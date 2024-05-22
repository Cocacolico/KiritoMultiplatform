package es.kirito.kirito.login.domain

import es.kirito.kirito.core.data.dataStore.updatePreferenciasKirito
import es.kirito.kirito.core.data.database.Estaciones
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.core.data.network.KiritoRequest
import es.kirito.kirito.core.data.network.ResponseKiritoDTO
import es.kirito.kirito.core.data.utils.KiritoException
import es.kirito.kirito.core.data.utils.KiritoUserBlockedException
import es.kirito.kirito.core.data.utils.KiritoWrongTokenException
import es.kirito.kirito.login.data.network.ResponseRegisterUserDTO
import es.kirito.kirito.login.data.network.ResponseResidenciasDTO
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginRepository : KoinComponent {

    private val database: KiritoDatabase by inject()
    private val dao = database.kiritoDao()
    private val ktor = KiritoRequest()


    suspend fun getResidencias(): ResponseKiritoDTO<ResponseResidenciasDTO> {
        return ktor.getResidencias()
    }

    suspend fun registerNewUser(
        residenciaSeleccionada: String,
        datosUsuario: RegisterData,
        tokenFCM: String
    ): ResponseKiritoDTO<ResponseRegisterUserDTO> {
        return ktor.requestRegistro(residenciaSeleccionada, datosUsuario, tokenFCM)
    }

    suspend fun getMyKiritoToken(
        usuario: String,
        password: String,
        nombreDispositivo: String,
        tokenFCM: String,
        residenciaUrl: String?
    ){
        val respuesta = ktor.requestLogin(usuario, password, nombreDispositivo, tokenFCM, residenciaUrl)

        if (respuesta.error.errorCode == "0") { // Login exitoso
            val tiempoBloqueado = respuesta.respuesta?.seconds
            if (tiempoBloqueado != null) { // Comprobamos que el usuario no esté bloqueado
                throw KiritoUserBlockedException("Bloqueado durante $tiempoBloqueado segundos.")
            }

            val kiritoToken = respuesta.respuesta?.login ?: throw KiritoWrongTokenException()
           // println("Login correcto, id es ${kiritoToken.id_usuario} y token es ${kiritoToken.token}")

            updatePreferenciasKirito { appSettings ->
                appSettings.copy(
                    matricula = usuario,
                    userId = kiritoToken.id_usuario.toLongOrNull() ?: -2L,
                    token = kiritoToken.token
                )
            }
        } else {
            when (respuesta.error.errorCode) {
                "13" -> respuesta // Usuario pendiente de autorizar
                else -> throw KiritoException("Error: ${respuesta.error}")
            }
        }
    }

    val estaciones = dao.getAllEstaciones()

    suspend fun refreshEstaciones() {
        listOf(
            Estaciones(
                nombre = "Estación Central",
                acronimo = "EC",
                numero = "001",
                longitud = -58.3816f,
                latitud = -34.6037f,
                esDelGrafico = true
            ), Estaciones(
                nombre = "Estación Norte",
                acronimo = "EN",
                numero = "002",
                longitud = -58.3838f,
                latitud = -34.6158f,
                esDelGrafico = false
            ), Estaciones(
                nombre = "Estación Sur",
                acronimo = "ES",
                numero = "003",
                longitud = -58.3912f,
                latitud = -34.6262f,
                esDelGrafico = true
            ), Estaciones(
                nombre = "Estación Este",
                acronimo = "EE",
                numero = "004",
                longitud = -58.3700f,
                latitud = -34.6032f,
                esDelGrafico = false
            )
        ).forEach {
            dao.insertEstacion(it)
        }
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