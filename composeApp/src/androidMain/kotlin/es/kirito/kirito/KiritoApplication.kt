package es.kirito.kirito

import android.app.Application
import es.kirito.kirito.core.data.di.appModule
import es.kirito.kirito.core.data.di.initKoin
import es.kirito.kirito.core.data.utils.ApplicationComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent

class KiritoApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        ApplicationComponent.init()

        initKoin{

            androidContext(this@KiritoApplication)
            modules(appModule)
        }
    }
}

