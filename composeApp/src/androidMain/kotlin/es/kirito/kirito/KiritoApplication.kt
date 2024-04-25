package es.kirito.kirito

import android.app.Application
import es.kirito.kirito.core.data.utils.ApplicationComponent

class KiritoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ApplicationComponent.init()
    }
}