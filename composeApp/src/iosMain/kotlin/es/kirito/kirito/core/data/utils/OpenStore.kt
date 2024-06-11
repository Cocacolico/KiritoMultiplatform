package es.kirito.kirito.core.data.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openStore() {
    val appStoreLink = "https://apps.apple.com/es/" //TODO Poner el enlace a Kirito
    val url = NSURL(string = appStoreLink)

    if(UIApplication.sharedApplication.canOpenURL(url)) {
        UIApplication.sharedApplication.openURL(url)
    }
}