package es.kirito.kirito.core.data.di

import android.content.Context
import androidx.room.ExperimentalRoomApi
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.core.data.database.dbFileName
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.concurrent.TimeUnit


actual fun platformModule(): Module = module {
  //  single { Android.create() }//Esto estaba originalmente, es de Ktor.
  //  single { dataStore(get()) }

    single<KiritoDatabase> { getKiritoDatabase(get()) }

}

@OptIn(ExperimentalRoomApi::class)
fun getKiritoDatabase(
    context: Context
): KiritoDatabase {
    val dbFile = context.getDatabasePath(dbFileName)
    return Room.databaseBuilder<KiritoDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
        .setAutoCloseTimeout(0, TimeUnit.SECONDS)//No tocarlo hasta que lo arreglen!!
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}



//fun dataStore(context: Context): DataStore<Preferences> =
//    createDataStore(
//        producePath = { context.filesDir.resolve("fpl.preferences_pb").absolutePath }
//    )