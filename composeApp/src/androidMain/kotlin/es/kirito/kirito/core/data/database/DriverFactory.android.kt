@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package es.kirito.kirito.core.data.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import es.kirito.kirito.core.data.sqldelight.KiritoDatabase

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(KiritoDatabase.Schema, context, "test.db")
    }
}