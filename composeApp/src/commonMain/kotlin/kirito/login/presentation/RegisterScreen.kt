package kirito.login.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.ExperimentalMaterial3Api
import kirito.core.presentation.components.MyTextStd
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun RegisterScreen() {
    val viewModel = RegisterViewModel()

    Surface(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            MyTextStd("Enhorabuena! Has llegado a la Screen de registro")
            Button(
                onClick = { viewModel.onClickButtonVolver()}
            ) {
                MyTextStd("Volver a Login")
            }
        }
}