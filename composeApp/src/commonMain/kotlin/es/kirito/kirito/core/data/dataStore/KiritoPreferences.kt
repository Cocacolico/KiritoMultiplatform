package es.kirito.kirito.core.data.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer


//class KiritoPreferencesImpl(
//    private val dataStore: DataStore<Preferences>
//)  {
//
//    private companion object {
//        private const val PREFS_TAG_KEY = "KiritoPreferences"
//        private const val IS_DARK_MODE_ENABLED = "prefsBoolean"
//    }
//
//    private val darkModeKey = booleanPreferencesKey("$PREFS_TAG_KEY$IS_DARK_MODE_ENABLED")
//
//    suspend fun isDarkModeEnabled() = dataStore.data.map { preferences ->
//        preferences[darkModeKey] ?: false
//    }.first()
//
//    suspend fun changeDarkMode(isEnabled : Boolean) = dataStore.edit { preferences ->
//        preferences[darkModeKey] = isEnabled
//    }
//}

class KiritoPreferencesImpl(
    private val dataStore: DataStore<Preferences>
)  {

    private companion object {
        private const val PREFS_TAG_KEY = "KiritoPreferences"
        private const val IS_DARK_MODE_ENABLED = "prefsBoolean"
    }

    private val darkModeKey = booleanPreferencesKey("$PREFS_TAG_KEY$IS_DARK_MODE_ENABLED")

    suspend fun isDarkModeEnabled() = dataStore.data.map { preferences ->
        preferences[darkModeKey] ?: false
    }.first()

    suspend fun changeDarkMode(isEnabled : Boolean) = dataStore.edit { preferences ->
        preferences[darkModeKey] = isEnabled
    }
}



/**
 * Ajustes de la app y del usuario.
 * @param estoyInicializado si true, entonces ya se ha migrado toodo de sharedPreferences.
 * @param lastAutoDcom Año.
 * @param nightMode 0 Dia, 1 Noche, 2 Como el sistema.
 * @param novedades 3 actual.
 * @param alarmFirstTime El automatismo que pone la alarma de 1 hora por primera vez.
 * @param language "es" español, "ca" catalán, "null" es sin configurar, se pondrá el del sistema o castellano por defecto.
 * @param mostrarMensajesAyuda Si true, permite que los mensajes de ayuda se muestren,
 * ponlo a false para que no se vea ninguno.
 * */
@Serializable
data class AppSettings(
    val estoyInicializado: Boolean = false,
    val matricula: String = "",
    val password: String = "",
    val token: String = "",
    val nightMode: Int = 2,
    val residenciaName: String = "",
    val residenciaURL: String = "",
    val userId: Long = 0,
    val language: String = "null",
    val preLogin: Boolean = false,
    val installDT: Long = 0L,
    val alarm1: Int = -1,
    val alarm2: Int = -1,
    val alarm3: Int = -1,
    val alarmRefri: Int = -1,
    val refriParams: String = "",
    val novedades: Int = 0,
    val lastAutoDcom: Int = 0,
    val autoDateDiasEsp: Long = 0L,
    val alarmFirstTime: Boolean = false,
    val weatherUpdated: Long = 0L,
    val lastTryUpdateDB: Long = 0L,
    val mostrarMensajesAyuda: Boolean = true,
    val ayudaTablonAnuncios: Boolean = true,
)


