package es.kirito.kirito.core.data.database

import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import kotlin.concurrent.Volatile

//Primero introducimos la tabla en entities.
//Versión: Cambiar SIEMPRE que modificamos el schema.
//exportSchema: Por defecto es true, guarda la base de datos en una carpeta.
@Database(
    entities = [GrGraficos::class],
    version = 1,
    exportSchema = true
)
abstract class KiritoDatabase : RoomDatabase() {

    abstract fun kiritoDao(): KiritoDao

}