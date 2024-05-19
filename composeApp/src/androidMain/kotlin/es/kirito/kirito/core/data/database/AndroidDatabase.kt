package es.kirito.kirito.core.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilderAndroid(ctx: Context): RoomDatabase.Builder<KiritoDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("kiritoDatabase.db")
    return Room.databaseBuilder<KiritoDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}