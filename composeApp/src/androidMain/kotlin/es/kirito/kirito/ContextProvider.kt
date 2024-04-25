package es.kirito.kirito

import android.content.Context
import androidx.startup.Initializer

internal lateinit var applicationContext: Context
    private set

data object ContextProviderInitializer

//Puede haber avisos de que hay memory leaks, pero es el contexto de la app, así que vive junto con ella.
class ContextProvider: Initializer<ContextProviderInitializer> {
    override fun create(context: Context): ContextProviderInitializer {
        applicationContext = context.applicationContext
        return ContextProviderInitializer
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}