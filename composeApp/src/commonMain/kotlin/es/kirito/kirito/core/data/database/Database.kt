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
    exportSchema = false
)
abstract class KiritoDatabase : RoomDatabase() {
    abstract fun kiritoDao(): KiritoDao
}

internal const val dbFileName = "kiritoDatabase.db"
