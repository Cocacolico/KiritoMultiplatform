package es.kirito.kirito.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

//Primero introducimos la tabla en entities.
//Versión: Cambiar SIEMPRE que modificamos el schema.
//exportSchema: Por defecto es true, guarda la base de datos en una carpeta.
@Database(
    entities = [ConfiguracionAPK::class, CuDetalle::class, MyKiritoUser::class,
        LsUsers::class, OtFestivo::class, GrTareas::class, GrExcelIF::class, GrGraficos::class,
        CuHistorial::class, CuDiasIniciales::class, GrEquivalencias::class, GrNotasTren::class,
        GrNotasTurno::class, OtTeleindicadores::class, TurnoCompi::class,
        UpdatedTables::class, OtMensajesAdmin::class, Estaciones::class, ColoresHoraTurnos::class,
        OtColoresTrenes::class, CaPeticiones::class, Clima::class, Localizador::class,
        TelefonoImportante::class, TablonAnuncios::class],
    version = 2,
    exportSchema = true
)
abstract class KiritoDatabase : RoomDatabase() {
    abstract fun kiritoDao(): KiritoDao
}

internal const val dbFileName = "kiritoDatabase.db"


//TODO: Original, borrar si va room con koin.
//fun getKiritoDatabase(
//    builder: RoomDatabase.Builder<KiritoDatabase>
//): KiritoDatabase {
//    return builder
//        .fallbackToDestructiveMigrationOnDowngrade(true)
//        .setDriver(BundledSQLiteDriver())
//        .setQueryCoroutineContext(Dispatchers.IO)
//        .build()
//}