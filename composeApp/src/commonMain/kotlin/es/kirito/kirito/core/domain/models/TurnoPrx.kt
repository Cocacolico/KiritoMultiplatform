package es.kirito.kirito.core.domain.models

data class TurnoPrxTr(
    var idDetalle: Long,
    var idUsuario: Int,
    var fecha: Long,
    var turno: String,
    var tipo: String,
    var notas: String?,
    var nombreDebe: String?,
    var idGrafico: Long?,
    var sitioOrigen: String?,
    var horaOrigen: Long?,
    var sitioFin: String?,
    var horaFin: Long?,
    var diaSemana: String?,
    var libra: Int?,
    var comj: Int?,
    var indicador: Int?,
    var equivalencia: String?,
    var color: Long?
){
    constructor() : this(
        idDetalle = 0,
        idUsuario = 0,
        fecha = 0,
        turno = "",
        tipo = "",
        notas = "",
        nombreDebe = "",
        idGrafico = 0,
        sitioOrigen = "",
        horaOrigen = 0,
        sitioFin = "",
        horaFin = 0,
        diaSemana = "",
        libra = 0,
        comj = 0,
        indicador = 0,
        equivalencia = "",
        color = 0,
    )
}