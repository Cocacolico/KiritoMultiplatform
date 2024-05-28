package es.kirito.kirito.core.domain.models

data class UserConfig(
    var id: Long,
    var username: String,
    var email: String,
    var name: String,
    var surname: String,
    var mostrarTelfTrabajo: String,
    var mostrarTelfPersonal: String,
    var cambiosActivados: String,
    var cambiosActivadosCuando: Long?,
    var recibirEmailNotificaciones: String,
    var mostrarCuadros: String,
    var mostrarCuadrosCuando: String,
)