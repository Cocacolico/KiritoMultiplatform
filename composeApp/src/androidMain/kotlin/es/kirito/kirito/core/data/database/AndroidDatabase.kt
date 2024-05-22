package es.kirito.kirito.core.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase


//TODO: Borrar archivo si va room con koin.
//fun getDatabaseBuilderAndroid(ctx: Context): RoomDatabase.Builder<KiritoDatabase> {
//    val appContext = ctx.applicationContext
//    val dbFile = appContext.getDatabasePath("kiritoDatabase.db")
//    return Room.databaseBuilder<KiritoDatabase>(
//        context = appContext,
//        name = dbFile.absolutePath
//    )
//}