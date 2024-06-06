package es.kirito.kirito.core.domain.models

data class GrTareaBuscador(
    var id: Long,
    var idGrafico: Long,
    var turno: String, //Los descansos son el número.
    var ordenServicio: Int,//Numero de tarea dentro del turno.
    var servicio: String,//Frase que describe lo que hacer. D en descanso. Puede ser "".
    var tipoServicio: String?, //T=Tren, M=Movimiento, "" si no es nada.
    var diaSemana: String?,
    var sitioOrigen: String?,
    var sOrigenCorto: String?,
    var horaOrigen: Int?, //Time.
    var sitioFin: String?,
    var sFinCorto: String?,
    var horaFin: Int?,
    var vehiculo: String?,
    var observaciones: String?,
    var inserted: Long?,//Datetime.
    var notasTren: String?,
)