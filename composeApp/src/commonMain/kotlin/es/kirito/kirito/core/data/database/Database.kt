package es.kirito.kirito.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

//Primero introducimos la tabla en entities.
//Versión: Cambiar SIEMPRE que modificamos el schema.
//exportSchema: Por defecto es true, guarda la base de datos en una carpeta.
@Database(
    entities = [Configuracion_APK::class, Cu_detalles::class, MyKiritoUser::class,
        LsUsers::class, Ot_festivos::class, GrTareas::class, GrExcelIF::class, GrGraficos::class,
        Cu_historial::class, CuDiasIniciales::class, GrEquivalencias::class, GrNotasTren::class,
        GrNotasTurno::class, OtTeleindicadores::class, TurnosCompis::class,
        UpdatedTables::class, OtMensajesAdmin::class, Estaciones::class, ColoresHoraTurnos::class,
        OtColoresTrenes::class, CaPeticiones::class, Clima::class, Localizador::class,
        TelefonoImportante::class, TablonAnuncios::class],
    version = 1,
    exportSchema = true
)
abstract class KiritoDatabase : RoomDatabase() {
    abstract fun kiritoDao(): KiritoDao
}

fun getKiritoDatabase(
    builder: RoomDatabase.Builder<KiritoDatabase>
): KiritoDatabase {
    return builder
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}