package es.kirito.kirito.core.data.network

import es.kirito.kirito.core.data.dataStore.preferenciasKirito
import es.kirito.kirito.core.data.network.models.RequestAnioDTO
import es.kirito.kirito.core.data.network.models.RequestAnioUpdatedDTO
import es.kirito.kirito.core.data.network.models.RequestComplementosGraficoDTO
import es.kirito.kirito.core.data.network.models.RequestEditarTurnoDTO
import es.kirito.kirito.core.data.network.models.RequestIncluidosDTO
import es.kirito.kirito.core.data.network.models.RequestSimpleDTO
import es.kirito.kirito.core.data.network.models.RequestUpdatedDTO
import es.kirito.kirito.core.data.network.models.ResponseKiritoDTO
import es.kirito.kirito.login.data.network.RequestLoginDTO
import es.kirito.kirito.login.data.network.RequestRegisterUserDTO
import es.kirito.kirito.login.data.network.ResponseLoginDTO
import es.kirito.kirito.login.data.network.ResponseOtEstacionesDTO
import es.kirito.kirito.login.data.network.ResponseRegisterUserDTO
import es.kirito.kirito.login.data.network.ResponseResidenciasDTO
import es.kirito.kirito.login.domain.RegisterData
import es.kirito.kirito.precarga.data.network.models.RequestGraficoDTO
import es.kirito.kirito.precarga.data.network.models.RequestStationCoordinatesDTO
import es.kirito.kirito.precarga.data.network.models.RequestTurnosCompiDTO
import es.kirito.kirito.precarga.data.network.models.ResponseCaPeticionesDTO
import es.kirito.kirito.precarga.data.network.models.ResponseColoresTrenesDTO
import es.kirito.kirito.precarga.data.network.models.ResponseComplementosGraficoDTO
import es.kirito.kirito.core.data.network.models.ResponseCuDetallesDTO
import es.kirito.kirito.precarga.data.network.models.ResponseCuHistorialDTO
import es.kirito.kirito.precarga.data.network.models.ResponseDelAndUpdElementsDTO
import es.kirito.kirito.precarga.data.network.models.ResponseDiasInicialesDTO
import es.kirito.kirito.precarga.data.network.models.ResponseEquivalenciasDTO
import es.kirito.kirito.precarga.data.network.models.ResponseExcelIfDTO
import es.kirito.kirito.precarga.data.network.models.ResponseExcesosGraficoDTO
import es.kirito.kirito.precarga.data.network.models.ResponseGeneralRefreshDTO
import es.kirito.kirito.precarga.data.network.models.ResponseGrGraficosDTO
import es.kirito.kirito.precarga.data.network.models.ResponseGrTareasDTO
import es.kirito.kirito.precarga.data.network.models.ResponseLocalizadoresDTO
import es.kirito.kirito.precarga.data.network.models.ResponseMensajesAdminDTO
import es.kirito.kirito.precarga.data.network.models.ResponseNotasTrenDTO
import es.kirito.kirito.precarga.data.network.models.ResponseNotasTurnoDTO
import es.kirito.kirito.precarga.data.network.models.ResponseOtFestivosDTO
import es.kirito.kirito.precarga.data.network.models.ResponseOtTablonAnunciosDTO
import es.kirito.kirito.precarga.data.network.models.ResponseStationCoordinatesDTO
import es.kirito.kirito.precarga.data.network.models.ResponseTelefonoEmpresaDTO
import es.kirito.kirito.precarga.data.network.models.ResponseTeleindicadorDTO
import es.kirito.kirito.precarga.data.network.models.ResponseTurnoDeCompiDTO
import es.kirito.kirito.precarga.data.network.models.ResponseUserDTO
import es.kirito.kirito.precarga.data.network.models.ResponseWeatherInfoDTO
import es.kirito.kirito.turnos.data.network.models.RequestSubirCuadroVacioDTO
import es.kirito.kirito.turnos.data.network.models.ResponseCuadroVacioDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

class KiritoRequest {

    private lateinit var residencia: String
    private val endpointInicial = "https://kirito.es/atc/api.php"
    private lateinit var endpoint: String
    private val appName = "Kiritroid"
    private val appVersion =
        "180" //TODO: Cambiar esto y hacerlo KMP  BuildConfig.VERSION_CODE.toString()
    //https://stackoverflow.com/questions/74743976/kotlin-multiplatform-accessing-build-variables-in-code

    suspend fun getResidencias(): ResponseKiritoDTO<ResponseResidenciasDTO> {
        return post<RequestSimpleDTO, ResponseResidenciasDTO>(
            RequestSimpleDTO("residencias"),
            esInicial = true
        )
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
                workPhoneExt = datosUsuario.workPhoneExt,
                workPhone = datosUsuario.workPhone,
                personalPhone = datosUsuario.personalPhone,
                mostrarTelfTrabajo = datosUsuario.mostrarTelfTrabajo,
                mostrarTelfPersonal = datosUsuario.mostrarTelfPersonal,
                comentariosAlAdmin = datosUsuario.comentariosAlAdmin,
                password = datosUsuario.password
            )
        )
    }

    suspend fun requestLogin(
        usuario: String,
        password: String,
        nombreDispositivo: String,
        tokenFCM: String,
        residenciaUrl: String?
    ): ResponseKiritoDTO<ResponseLoginDTO> {
        return post<RequestLoginDTO, ResponseLoginDTO>(
            RequestLoginDTO(
                peticion = "usuarios.login",
                usuario = usuario,
                password = password,
                descripcion_dispositivo = nombreDispositivo,
                tokenFCM = tokenFCM
            ),
            residenciaPersonalizada = residenciaUrl
        )
    }


    suspend fun requestOtFestivos(request: RequestUpdatedDTO): ResponseKiritoDTO<List<ResponseOtFestivosDTO>> {
        return post<RequestUpdatedDTO, List<ResponseOtFestivosDTO>>(request)
    }

    suspend fun requestGraficos(request: RequestUpdatedDTO): ResponseKiritoDTO<List<ResponseGrGraficosDTO>> {
        return post<RequestUpdatedDTO, List<ResponseGrGraficosDTO>>(request)
    }

    suspend fun requestHistorial(request: RequestUpdatedDTO): ResponseKiritoDTO<List<ResponseCuHistorialDTO>> {
        return post<RequestUpdatedDTO, List<ResponseCuHistorialDTO>>(request)
    }

    suspend fun requestCuDetalles(request: RequestUpdatedDTO): ResponseKiritoDTO<List<ResponseCuDetallesDTO>> {
        return post<RequestUpdatedDTO, List<ResponseCuDetallesDTO>>(request)
    }

    suspend fun requestExcesosGrafico(request: RequestAnioDTO): ResponseKiritoDTO<List<ResponseExcesosGraficoDTO>> {
        return post<RequestAnioDTO, List<ResponseExcesosGraficoDTO>>(request)
    }

    suspend fun requestMensajesAdmin(request: RequestUpdatedDTO): ResponseKiritoDTO<List<ResponseMensajesAdminDTO>> {
        return post<RequestUpdatedDTO, List<ResponseMensajesAdminDTO>>(request)
    }

    suspend fun requestColoresTrenes(request: RequestSimpleDTO): ResponseKiritoDTO<List<ResponseColoresTrenesDTO>> {
        return post<RequestSimpleDTO, List<ResponseColoresTrenesDTO>>(request)
    }

    suspend fun requestCaPeticiones(request: RequestUpdatedDTO): ResponseKiritoDTO<List<ResponseCaPeticionesDTO>> {
        return post<RequestUpdatedDTO, List<ResponseCaPeticionesDTO>>(request)
    }

    suspend fun requestTelefonosEmpresa(request: RequestUpdatedDTO): ResponseKiritoDTO<List<ResponseTelefonoEmpresaDTO>> {
        return post<RequestUpdatedDTO, List<ResponseTelefonoEmpresaDTO>>(request)
    }

    suspend fun requestOtTablonAnuncios(request: RequestUpdatedDTO): ResponseKiritoDTO<List<ResponseOtTablonAnunciosDTO>> {
        return post<RequestUpdatedDTO, List<ResponseOtTablonAnunciosDTO>>(request)
    }

    suspend fun requestDiasIniciales(request: RequestAnioDTO): ResponseKiritoDTO<List<ResponseDiasInicialesDTO>> {
        return post<RequestAnioDTO, List<ResponseDiasInicialesDTO>>(request)
    }

    suspend fun requestTeleindicadores(request: RequestSimpleDTO): ResponseKiritoDTO<List<ResponseTeleindicadorDTO>> {
        return post<RequestSimpleDTO, List<ResponseTeleindicadorDTO>>(request)
    }

    suspend fun requestUsuarios(request: RequestUpdatedDTO): ResponseKiritoDTO<List<ResponseUserDTO>> {
        return post<RequestUpdatedDTO, List<ResponseUserDTO>>(request)
    }

    suspend fun requestTurnosDeUnCompi(request: RequestTurnosCompiDTO): ResponseKiritoDTO<List<ResponseTurnoDeCompiDTO>> {
        return post<RequestTurnosCompiDTO, List<ResponseTurnoDeCompiDTO>>(request)
    }

    suspend fun requestOtEstaciones(salida: RequestSimpleDTO): ResponseKiritoDTO<List<ResponseOtEstacionesDTO>> {
        return post<RequestSimpleDTO, List<ResponseOtEstacionesDTO>>(salida)
    }

    suspend fun requestOtEstacionesInc(salida: RequestIncluidosDTO): ResponseKiritoDTO<List<ResponseOtEstacionesDTO>> {
        return post<RequestIncluidosDTO, List<ResponseOtEstacionesDTO>>(salida)
    }

    suspend fun requestExcelIf(salida: RequestGraficoDTO): ResponseKiritoDTO<List<ResponseExcelIfDTO>> {
        return post<RequestGraficoDTO, List<ResponseExcelIfDTO>>(salida)
    }

    suspend fun requestGrTareas(salida: RequestGraficoDTO): ResponseKiritoDTO<List<ResponseGrTareasDTO>> {
        return post<RequestGraficoDTO, List<ResponseGrTareasDTO>>(salida)
    }

    suspend fun requestNotasTren(salida: RequestGraficoDTO): ResponseKiritoDTO<List<ResponseNotasTrenDTO>> {
        return post<RequestGraficoDTO, List<ResponseNotasTrenDTO>>(salida)
    }

    suspend fun requestNotasTurno(salida: RequestGraficoDTO): ResponseKiritoDTO<List<ResponseNotasTurnoDTO>> {
        return post<RequestGraficoDTO, List<ResponseNotasTurnoDTO>>(salida)
    }

    suspend fun requestEquivalencias(salida: RequestGraficoDTO): ResponseKiritoDTO<List<ResponseEquivalenciasDTO>> {
        return post<RequestGraficoDTO, List<ResponseEquivalenciasDTO>>(salida)
    }

    suspend fun requestTurnosCompis(salida: RequestAnioUpdatedDTO): ResponseKiritoDTO<List<ResponseTurnoDeCompiDTO>> {
        return post<RequestAnioUpdatedDTO, List<ResponseTurnoDeCompiDTO>>(salida)
    }

    suspend fun requestStationCoordinates(salida: RequestStationCoordinatesDTO): ResponseKiritoDTO<ResponseStationCoordinatesDTO> {
        return post<RequestStationCoordinatesDTO, ResponseStationCoordinatesDTO>(salida)
    }

    suspend fun requestWeatherInfo(url: String): ResponseWeatherInfoDTO {
        return getClienteJson().get(url).body() as ResponseWeatherInfoDTO
    }

    suspend fun requestLocalizadores(request: RequestUpdatedDTO): ResponseKiritoDTO<List<ResponseLocalizadoresDTO>> {
        return post<RequestUpdatedDTO, List<ResponseLocalizadoresDTO>>(request)
    }

    suspend fun requestSimpleEmptyResponse(request: RequestSimpleDTO): ResponseKiritoDTO<Unit?> {
        return post<RequestSimpleDTO, Unit?>(request)
    }

    suspend fun requestDelAndUpdElements(request: RequestSimpleDTO): ResponseKiritoDTO<List<ResponseDelAndUpdElementsDTO>> {
        return post<RequestSimpleDTO, List<ResponseDelAndUpdElementsDTO>>(request)
    }

    suspend fun requestSubirCuadroVacio(request: RequestSubirCuadroVacioDTO): ResponseKiritoDTO<List<ResponseCuadroVacioDTO>> {
        return post<RequestSubirCuadroVacioDTO, List<ResponseCuadroVacioDTO>>(request)
    }

    suspend fun requestComplementosGrafico(request: RequestComplementosGraficoDTO): ResponseKiritoDTO<List<ResponseComplementosGraficoDTO>> {
        return post<RequestComplementosGraficoDTO, List<ResponseComplementosGraficoDTO>>(request)
    }

    suspend fun requestUpdateElementosGlobales(request: RequestUpdatedDTO): ResponseKiritoDTO<ResponseGeneralRefreshDTO?> {
        return post<RequestUpdatedDTO, ResponseGeneralRefreshDTO?>(request)
    }

    suspend fun requestUpdateOneShift(request: RequestEditarTurnoDTO): ResponseKiritoDTO<ResponseCuDetallesDTO?> {
        return post<RequestEditarTurnoDTO, ResponseCuDetallesDTO?>(request)
    }


    // suspend fun post(request: Map<String, String>): HttpResponse {//Por si no va en ios el reified.
    // https://github.com/JetBrains/compose-multiplatform/issues/3147
    private suspend inline fun <reified R, reified T> post(
        request: R,
        esInicial: Boolean = false,
        residenciaPersonalizada: String? = null,
    ): ResponseKiritoDTO<T> {
        val preferencias = preferenciasKirito.first()
        val tokenKirito = preferencias.token
        residencia = preferencias.residenciaURL
        endpoint = if (residenciaPersonalizada == null)
            "https://kirito.es/$residencia/api.php"
        else
            "https://kirito.es/$residenciaPersonalizada/api.php"

        val cliente = getClienteJson()
        val url = if (esInicial) endpointInicial else endpoint

        val response = cliente.post(url) {
            contentType(ContentType.Application.Json)
            header(key = "AppName", value = appName)
            header(key = "AppVersion", value = appVersion)
            bearerAuth(tokenKirito)
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