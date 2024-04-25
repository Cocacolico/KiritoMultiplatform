package es.kirito.kirito.core.data.dataStore

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import es.kirito.kirito.applicationContext
import kotlinx.coroutines.CoroutineScope
import java.io.File

actual fun dataStorePreferences(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>?,
    coroutineScope: CoroutineScope,
    migrations: List<DataMigration<Preferences>>
): DataStore<Preferences>  = createDataStoreWithDefaults(
    corruptionHandler = corruptionHandler,
    migrations = migrations,
    coroutineScope = coroutineScope,
    path = {
        //TODO: Si hay crash, este file quizá no tiene el import correcto.
        File(applicationContext.filesDir, "datastore/$SETTINGS_PREFERENCES").path
    }
)