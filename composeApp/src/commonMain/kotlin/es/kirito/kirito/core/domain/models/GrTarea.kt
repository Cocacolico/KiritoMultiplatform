package es.kirito.kirito.core.domain.models


data class GrTarea(
    var id: Long,
    var idGrafico: Long,
    var turno: String, //Los descansos son el número.
    var ordenServicio: Int,//Numero de tarea dentro del turno.
    var servicio: String,//Frase que describe lo que hacer. D en descanso. Puede ser "".
    var tipoServicio: String?, //T=Tren, M=Movimiento, "" si no es nada.
    var diaSemana: String?,
    var sitioOrigen: String?,
    var horaOrigen: Int?, //Time.
    var sitioFin: String?,
    var horaFin: Int?,
    var vehiculo: String?,
    var observaciones: String?,
    var inserted: Long?,//Datetime.
    var notasTren: String?,
    var tempO: Float? = null,
    var probO: Int? = null,
    var lluviaO: Float? = null,
    var nieveO: Float? = null,
    var nubladoO: Int? = null,
    var vientoO: Float? = null,
    var visibilidadO: Int? = null,
    var tempF: Float? = null,
    var probF: Int? = null,
    var lluviaF: Float? = null,
    var nieveF: Float? = null,
    var nubladoF: Int? = null,
    var vientoF: Float? = null,
    var visibilidadF: Int? = null,
    var tickMinute: Int? = null,
){constructor() : this(0L, 0L, "", 0, "", null,
    null, null, null,null,null,null,null,null,null,null)


}