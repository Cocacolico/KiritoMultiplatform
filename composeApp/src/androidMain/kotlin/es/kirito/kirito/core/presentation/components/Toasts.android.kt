package es.kirito.kirito.core.presentation.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources._0

@Composable
actual fun ShortToast(message: String) {
    val context = LocalContext.current
    LaunchedEffect(Unit){
        if (message != null && message != "0")
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }
}

@Composable
actual fun LongToast(message: String?) {
    val context = LocalContext.current
    LaunchedEffect(message){
        if (message != null && message != "0")
            Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
}