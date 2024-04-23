package kirito.core.data.network

import es.kirito.kirito.login.data.network.ResponseResidenciasDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
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