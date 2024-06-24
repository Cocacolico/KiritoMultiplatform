package es.kirito.kirito.core.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//TRAS TOCAR AQUÍ, ACUÉRDATE DE ACTUALIZAR LA LISTA EN KIRITODATABASE.KT


/** GR_listado_graficos **/
@Entity(tableName = "tabla_gr_graficos")
data class GrGraficos(
    @PrimaryKey
    var idGrafico: Long,
    var fechaInicio: Long?,
    var fechaFinal: Long?,
    var descripcion: String?,
    var ficheroGrafico: String?,
    var ficheroAnexo: String?,
    var enlaceGraficoGdrive: String?,
    var nombreGraficoGdrive: String?,
    var enlaceAnexoGdrive: String?,
    var nombreAnexoGdrive: String?,
    var fechaUltimoCambio: Long?
)

@Entity(tableName = "tabla_equivalencias", primaryKeys = ["idGrafico", "turno"])
data class GrEquivalencias(
    var idGrafico: Long,
    var turno: String,
    var equivalencia: String
)

@Entity(tableName = "tabla_notas_tren")
data class GrNotasTren(
    @PrimaryKey
    var id: Long,
    var idGrafico: Long,
    var tren: String,
    var lunes: Boolean = false,
    var martes: Boolean = false,
    var miercoles: Boolean = false,
    var jueves: Boolean = false,
    var viernes: Boolean = false,
    var sabado: Boolean = false,
    var domingo: Boolean = false,
    var festivo: Boolean = false,
    var nota: String,
)

@Entity(tableName = "tabla_notas_turno")
data class GrNotasTurno(
    @PrimaryKey
    var id: Long,
    var idGrafico: Long,
    var turno: String,
    var lunes: Boolean = false,
    var martes: Boolean = false,
    var miercoles: Boolean = false,
    var jueves: Boolean = false,
    var viernes: Boolean = false,
    var sabado: Boolean = false,
    var domingo: Boolean = false,
    var festivo: Boolean = false,
    var nota: String,
)


/** GR_detalles_mensual **/
@Entity(tableName = "tabla_gr_excel_if")
data class GrExcelIF(
    @PrimaryKey
    var id: Long,
    var idGrafico: Long,
    var numeroTurno: Int,
    var ordenTarea: Int,//1 es diario, 2 findes, puede cambiar.
    var lunes: Boolean = false,
    var martes: Boolean = false,
    var miercoles: Boolean = false,
    var jueves: Boolean = false,
    var viernes: Boolean = false,
    var sabado: Boolean = false,
    var domingo: Boolean = false,
    var festivo: Boolean = false,
    var comentarioAlTurno: String?,//El comentario que se le puede poner
    var turnoReal: String?,//Si es un descanso, pondrá D.
    var sitioOrigen: String?,
    var horaOrigen: Int?,
    var sitioFin: String?,
    var horaFin: Int?,
)

/** GR_detalles_libreta **/
@Entity(tableName = "tabla_gr_tareas")
data class GrTareas(
    @PrimaryKey
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
)


@Entity(tableName = "tabla_colores_trenes")
data class OtColoresTrenes(
    @PrimaryKey
    var filtro: String,
    var color: String
)

@Entity(tableName = "tabla_ot_festivos")
data class OtFestivo(
    @PrimaryKey
    var idFestivo: Long,
    var fecha: Long,
    var descripcion: String?
)

@Entity(tableName = "tabla_estaciones")
data class Estaciones(
    @PrimaryKey
    var nombre: String,
    var acronimo: String,
    var numero: String,
    var longitud: Float?,
    var latitud: Float?,
    var esDelGrafico: Boolean = false,
)


@Entity(tableName = "tabla_teleindicadores")
data class OtTeleindicadores(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var tren: String,
    var codigo: String,
    var vehiculo: String,
    var notas: String?,
    var lunes: Boolean = false,
    var martes: Boolean = false,
    var miercoles: Boolean = false,
    var jueves: Boolean = false,
    var viernes: Boolean = false,
    var sabado: Boolean = false,
    var domingo: Boolean = false,
    var festivo: Boolean = false,
)

@Entity(tableName = "tabla_mensajes_de_admin")
data class OtMensajesAdmin(
    @PrimaryKey
    var id: Long,
    var titulo: String = "",
    var mensaje: String = "",
    var enviado: Long,
    var enviadoPor: String,
    /** 0 Sin leer, 1 Leído, -1 Leído y abierto, 2 Borrado, -2 Borrado y abierto. **/
    var estado: Int,
)


@Entity(tableName = "tabla_cu_detalle")
data class CuDetalle(
    @PrimaryKey
    var idDetalle: Long,
    var idUsuario: Long,
    var fecha: Long,//DATE
    var diaSemana: String?,
    var turno: String?,
    var tipo: String,
    var notas: String,
    var nombreDebe: String,
    var updated: Long?,//DATETIME
    var libra: Int?,
    var comj: Int?,
    var excesos: Int?,
    var mermas: Int?,
    @ColumnInfo(name = "excesosGrafico", defaultValue = "0")
    var excesosGrafico: Int?,
) {
    constructor() : this(
        idDetalle = -1L,
        idUsuario = -1L,
        fecha = -1L,
        diaSemana = null,
        turno = null,
        tipo = "",
        notas = "",
        nombreDebe = "",
        updated = null,
        libra = null,
        comj = null,
        excesos = null,
        mermas = null,
        excesosGrafico = null
    )
}


@Entity(tableName = "tabla_colores_hora_turnos")
data class ColoresHoraTurnos(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var color: Int,
    var horaInicio: Long
)

@Entity(tableName = "tabla_dias_iniciales", primaryKeys = ["anio", "tipo"])
data class CuDiasIniciales(
    var anio: Int,
    var tipo: String,
    var valor: Int
)

@Entity(tableName = "tabla_historial")
data class CuHistorial(
    @PrimaryKey
    var id: Long,
    var idDetalle: Long,
    var turno: String,
    var tipo: String,
    var nombreDebe: String?,
    var updated: Long//Datetime.
)


@Entity(tableName = "tabla_my_kirito_user")
data class MyKiritoUser(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var usuario: String,
    var password: String,
    var kiritoToken: String,
    var idDispositivo: String,
    var descripcionDispositivo: String,
    var recordarme: Boolean = false,
)


//En las etiquetas como @Entity, el nombre va siempre en minúsculas con guiones bajos.
@Entity(tableName = "tabla_configuracion_apk")
data class ConfiguracionAPK(
    @PrimaryKey
    var nombreConfiguracion: String = "",
    var valorConfiguracion: Int = 0,
)

@Entity(tableName = "tabla_updated_tables", primaryKeys = ["tableName", "year"])
data class UpdatedTables(
    var tableName: String,
    var year: Int = 0,
    var updated: Long?,
)


@Entity
data class CuDiaGanado(
    @PrimaryKey(autoGenerate = false)
    var id: Long,
    var idDetalle: Long,
    var idTipo: Long,
    var computo: Int,
)


@Entity
data class LsUsers(
    @PrimaryKey
    var id: Long,
    var username: String,
    var email: String,
    var name: String,
    var surname: String,
    var normalizedName: String,
    var normalizedSurname: String,
    var workPhoneExt: String,
    var workPhone: String,
    var personalPhone: String,
    var mostrarTelfTrabajo: String,
    var mostrarTelfPersonal: String,
    var photo: String,
    var created: Long?,//DATETIME C/S
    var lastLogin: Long?,//DATETIME C/S
    var disabled: String,
    var admin: String,
    var keyIcs: String,
    var keyAccessWeb: String,
    var comentariosAlAdmin: String,
    var cambiosActivados: String,
    var cambiosActivadosCuando: Long?,
    var recibirEmailNotificaciones: String,
    var mostrarCuadros: String,
    var mostrarCuadrosCuando: String,
    var notas: String,
    var peticionesDiarias: String
){
    constructor() : this(
        -1L,"","","","","","","","","","","","",null,null,"","","","","","",null,"","","","",""
    )
}

@Entity(tableName = "tabla_turno_compi")
data class TurnoCompi(
    @PrimaryKey
    var idDetalle: Long,
    var idUsuario: Long,
    var fecha: Long,
    var turno: String?,
    var tipo: String,
)

@Entity(tableName = "tabla_peticiones_ca")
data class CaPeticiones(
    @PrimaryKey
    var id: Long,
    var idUsuarioPide: Long,
    var turnoUsuarioPide: String,
    var idUsuarioRecibe: Long,
    var turnoUsuarioRecibe: String,
    var fecha: Long,
    var dtPeticion: Long,
    var dtRespuesta: Long?,
    var estado: String,
)

@Entity(tableName = "tabla_clima", primaryKeys = ["time","estacion"])
data class Clima(
    var time: Long,//Datetime
    var estacion: String,
    var temperatura: Float,//celsius
    var probabilidadLluvia: Int,//%
    var lluvia: Float,//mm
    var nieve: Float,//cm
    var nublado: Int,//%
    @ColumnInfo(name = "viento", defaultValue = "-1.0")
    var viento: Float,//Km/h
    @ColumnInfo(name = "visibilidad", defaultValue = "-1")
    var visibilidad: Int,//m
    var created: Long,//Datetime
)

@Entity(tableName = "tabla_localizadores", primaryKeys = ["fecha", "turno"])
data class Localizador(
    var fecha: Long,
    var turno: String,
    var localizador: String,
)

@Entity(tableName = "tabla_anuncios")
data class TablonAnuncios(
    @PrimaryKey
    var id: Long,
    var idUsuario: Long,
    var fecha: Long,
    var titulo: String,
    var explicacion: String,
    var etiqueta1: String?,
    var etiqueta2: String?,
    var etiqueta3: String?,
    var updated: Long?,
)

@Entity(tableName = "tabla_telefonos_importantes", primaryKeys = ["id"])
data class TelefonoImportante(
    var id: Long,
    var empresa: String,
    var tipoServicio: String,
    var nombre: String,
    var telefono1: Long,
    var telefono2: Long
){
    constructor() : this(
        -1L,"","","",-1L,-1L
    )
}