package es.kirito.kirito.core.data.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import es.kirito.kirito.core.data.dataStore.KiritoPreferencesImpl
import es.kirito.kirito.core.data.dataStore.dataStorePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

interface CoreComponent : CoroutinesComponent {
    val appPreferences: KiritoPreferencesImpl
}

internal class CoreComponentImpl internal constructor() : CoreComponent,
    CoroutinesComponent by CoroutinesComponentImpl.create() {

    private val dataStore: DataStore<Preferences> = dataStorePreferences(
        corruptionHandler = null,
        coroutineScope =
        CoroutineScope(applicationScope.coroutineContext + Dispatchers.IO),//Esperemos que esto vaya :(.
        //applicationScope + Dispatchers.IO,
        migrations = emptyList()
    )

    override val appPreferences : KiritoPreferencesImpl = KiritoPreferencesImpl(dataStore)
}