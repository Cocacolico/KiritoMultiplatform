@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package es.kirito.kirito.core.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import es.kirito.kirito.core.data.sqldelight.KiritoDatabase

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(KiritoDatabase.Schema, "test.db")
    }
}