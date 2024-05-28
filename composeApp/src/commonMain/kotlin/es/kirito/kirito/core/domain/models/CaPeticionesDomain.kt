package es.kirito.kirito.core.domain.models

data class CaPeticionesDomain(
    var id: Long,
    var idMiUser: Long,
    var idUsuarioPide: Long,
    var idUsuarioRecibe: Long,
    var nameUsuarioPide: String?,
    var nameUsuarioRecibe: String?,
    var turnoUsuarioPide: String,
    var turnoUsuarioRecibe: String,
    var turnoCompiPide: String?,
    var turnoCompiRecibe: String?,
    var miTurnoCD: String?,
    var fecha: Long,
    var dtPeticion: Long,
    var dtRespuesta: Long?,
    var estado: String,
)