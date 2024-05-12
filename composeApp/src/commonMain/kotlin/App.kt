import androidx.compose.runtime.*
import androidx.room.RoomDatabase
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.core.presentation.theme.KiritoTheme
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview

import es.kirito.kirito.login.presentation.LoginScreen
import es.kirito.kirito.login.presentation.RegisterScreen

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App(
    databaseBuilder: RoomDatabase.Builder<KiritoDatabase>,
) {
    KiritoTheme {

        LoginScreen()




        //Lo que había por defecto, puede valernos para ver código de otros.
//        var showContent by remember { mutableStateOf(false) }
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }

    }
}