package es.kirito.kirito.core.data.di

import androidx.room.Room
import androidx.room.RoomDatabase
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.core.data.database.dbFileName
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
//    single { Darwin.create() }//Ktor.
//    single { dataStore()}

    single<KiritoDatabase> { getKiritoDatabase() }

}


fun getKiritoDatabase(

): KiritoDatabase {
    val dbFilePath = NSHomeDirectory() + "/$dbFileName"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
        factory = { KiritoDatabase::class.instantiateImpl() }
    )
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}


//TODO: Héctor, en el tutorial, en vez de coger el dbFilePath como haces tú, lo hacía así.
//Por si acaso, lo dejo por si te hace falta.
//https://github.com/joreilly/FantasyPremierLeague/blob/main/common/src/iOSMain/kotlin/dev/johnoreilly/common/actual.kt

//private fun fileDirectory(): String {
//    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
//        directory = NSDocumentDirectory,
//        inDomain = NSUserDomainMask,
//        appropriateForURL = null,
//        create = false,
//        error = null,
//    )
//    return requireNotNull(documentDirectory).path!!
//}
