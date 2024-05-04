@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package es.kirito.kirito.core.data.database

import app.cash.sqldelight.db.SqlDriver
import es.kirito.kirito.core.data.sqldelight.KiritoDatabase


expect class DriverFactory {
   fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): KiritoDatabase {
    val driver = driverFactory.createDriver()
    val database = KiritoDatabase(driver)

    return database
}