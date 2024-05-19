package es.kirito.kirito.login.domain

data class RegisterData(
    var username: String,
    var email: String,
    var name: String,
    var surname: String,
    var work_phone_ext: String,
    var work_phone: String,
    var personal_phone: String,
    var mostrar_telf_trabajo: String,
    var mostrar_telf_personal: String,
    var comentariosAlAdmin: String,
    var password: String,
)
