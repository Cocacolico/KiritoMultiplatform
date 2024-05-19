package es.kirito.kirito.core.data.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import es.kirito.kirito.core.data.utils.coreComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class KiritoPreferencesImpl(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        private const val KIRITO_SETTINGS = "kiritoSettings"
    }

    private val kiritoSettings = stringPreferencesKey(KIRITO_SETTINGS)

    internal val kiritoPrefs = dataStore.data.map { preferences ->
        preferences[kiritoSettings] ?: "NADA"
    }.map { cadena ->
        if (cadena == "NADA")
            AppSettings()
        else
            Json.decodeFromString(cadena)
    }

   internal suspend fun savePrefs(settings: AppSettings) {
        val cadena = Json.encodeToString(settings)
        dataStore.edit { preferences ->
            preferences[kiritoSettings] = cadena
        }
    }
}

val preferenciasKirito = coreComponent.appPreferences.kiritoPrefs

suspend fun updatePreferenciasKirito(update: (AppSettings) -> AppSettings) {
    val settings = coreComponent.appPreferences.kiritoPrefs.first()
    val updatedSettings = update(settings)
    coreComponent.appPreferences.savePrefs(updatedSettings)
}





