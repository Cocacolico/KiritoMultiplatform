package es.kirito.kirito.core.domain.models

data class TelefonosUsuario(
    var username: String,
    var name: String,
    var surname: String,
    var workPhoneExt: String,
    var workPhone: String,
    var personalPhone: String,
    var mostrarTelfTrabajo: String,
    var mostrarTelfPersonal: String,
)