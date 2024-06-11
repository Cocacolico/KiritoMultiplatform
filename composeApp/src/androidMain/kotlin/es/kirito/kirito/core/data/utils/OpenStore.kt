package es.kirito.kirito.core.data.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import es.kirito.kirito.ContextProvider
import es.kirito.kirito.applicationContext

actual fun openStore() {
    val appPackageName = applicationContext.packageName
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        applicationContext.startActivity(
            intent
            //Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
        )
    } catch (e: ActivityNotFoundException) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName"))
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        applicationContext.startActivity(
            //Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName"))
            intent
        )
    }
}