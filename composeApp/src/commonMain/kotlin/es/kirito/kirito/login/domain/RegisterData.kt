package es.kirito.kirito.login.domain

data class RegisterData(
    var username: String,
    var email: String,
    var name: String,
    var surname: String,
    var workPhoneExt: String,
    var workPhone: String,
    var personalPhone: String,
    var mostrarTelfTrabajo: String,
    var mostrarTelfPersonal: String,
    var comentariosAlAdmin: String,
    var password: String,
)
