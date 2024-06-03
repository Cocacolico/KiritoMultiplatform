package es.kirito.kirito.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase


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
abstract class KiritoDatabase : RoomDatabase(), DB {
    abstract fun kiritoDao(): KiritoDao

    override fun clearAllTables() {
        super.clearAllTables()
    }
}

internal const val dbFileName = "kiritoDatabase.db"

// FIXME: Added a hack to resolve below issue:
// Class 'AppDatabase_Impl' is not abstract and does not implement abstract base class member 'clearAllTables'.
interface DB {
    fun clearAllTables(): Unit {}
}