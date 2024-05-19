package es.kirito.kirito.core.data.network

import es.kirito.kirito.login.data.network.RequestLoginDTO
import es.kirito.kirito.login.data.network.RequestRegisterUserDTO
import es.kirito.kirito.login.data.network.ResponseLoginDTO
import es.kirito.kirito.login.data.network.ResponseOtEstacionesDTO
import es.kirito.kirito.login.data.network.ResponseRegisterUserDTO
import es.kirito.kirito.login.data.network.ResponseResidenciasDTO
import es.kirito.kirito.login.domain.RegisterData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KiritoRequest {

    private val residencia = "chasca"  //TODO: get the correct residencia
    private val endpointInicial = "https://kirito.es/atc/api.php"
    private val endpoint = "https://kirito.es/$residencia/api.php"
    private val appName = "Kiritroid"
    private val appVersion = "180" //TODO: Cambiar esto y hacerlo KMP  BuildConfig.VERSION_CODE.toString()
    //https://stackoverflow.com/questions/74743976/kotlin-multiplatform-accessing-build-variables-in-code

    suspend fun getResidencias(): ResponseKiritoDTO<ResponseResidenciasDTO> {
        return post<RequestSimpleDTO, ResponseResidenciasDTO>(RequestSimpleDTO("residencias"), esInicial = true)
    }
    suspend fun requestRegistro(
        residenciaSeleccionada: String,
        datosUsuario: RegisterData,
        tokenFCM: String
    ): ResponseKiritoDTO<ResponseRegisterUserDTO> {
        return post<RequestRegisterUserDTO, ResponseRegisterUserDTO>(
            RequestRegisterUserDTO(
                peticion = "usuarios.registrar",
                username = datosUsuario.username,
                email = datosUsuario.email,
                name = datosUsuario.name,
                surname = datosUsuario.surname,
                work_phone_ext = datosUsuario.work_phone_ext,
                work_phone = datosUsuario.work_phone,
                personal_phone = datosUsuario.personal_phone,
                mostrar_telf_trabajo = datosUsuario.mostrar_telf_trabajo,
                mostrar_telf_personal = datosUsuario.mostrar_telf_personal,
                comentariosAlAdmin = datosUsuario.comentariosAlAdmin,
                password = datosUsuario.password
            )
        )
    }
    suspend fun requestLogin(
        usuario: String,
        password: String,
        nombreDispositivo: String,
        tokenFCM: String
    ): ResponseKiritoDTO<ResponseLoginDTO> {
        return post<RequestLoginDTO, ResponseLoginDTO>(
            RequestLoginDTO(
                peticion = "usuarios.login",
                usuario = usuario,
                password = password,
                descripcion_dispositivo = nombreDispositivo,
                tokenFCM = tokenFCM
            )
        )
    }
    suspend fun requestOtEstaciones(): ResponseKiritoDTO<ResponseOtEstacionesDTO> {
        return post<RequestSimpleDTO, ResponseOtEstacionesDTO>(
            RequestSimpleDTO("otros.obtener_estaciones")
        )

    }


    // suspend fun post(request: Map<String, String>): HttpResponse {//Por si no va en ios el reified.
    // https://github.com/JetBrains/compose-multiplatform/issues/3147
    private suspend inline fun <reified R, reified T> post(request: R, esInicial: Boolean = false): ResponseKiritoDTO<T> {
        val cliente = getClienteJson()

        val url = if (esInicial) endpointInicial else endpoint

        val response = cliente.post(url) {
            contentType(ContentType.Application.Json)
            header(key = "AppName", value = appName)
            header(key = "AppVersion", value = appVersion)
            setBody(request)
        }
        return response.body()
    }



    private fun getClienteJson() = HttpClient {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        //Darwin no acepta timeouts. No lo meto hasta que vea un método bueno para ambos.
//        install(HttpTimeout){
//            requestTimeoutMillis = timeout * 1000L
//        }

        //https://ktor.io/docs/client-serialization.html
        install(ContentNegotiation) {
            //Aquí qué extras o configuraciones puedes hacerle al json.
            // https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/basic-serialization.md#serializable-classes
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }
}