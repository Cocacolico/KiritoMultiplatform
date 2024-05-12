package es.kirito.kirito.core.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

fun getDatabaseBuilderIos(): RoomDatabase.Builder<KiritoDatabase> {
    val dbFilePath = NSHomeDirectory() + "/kiritoDatabase.db"
    return Room.databaseBuilder<KiritoDatabase>(
        name = dbFilePath,
        factory =  { KiritoDatabase::class.instantiateImpl() }  // IDE may show error but there is none.
    )
}