import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.room.RoomDatabase
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.core.data.database.getKiritoDatabase
import es.kirito.kirito.core.presentation.theme.KiritoTheme
import es.kirito.kirito.login.presentation.LoginScreen
import es.kirito.kirito.login.presentation.RegisterScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App(databaseBuilder: RoomDatabase.Builder<KiritoDatabase>) {
    KiritoTheme {
        val navController = rememberNavController()
        val database = getKiritoDatabase(databaseBuilder)
        NavHost(
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") {
                LoginScreen(navController, database)
            }
            composable("register") {
                RegisterScreen(navController, database)
            }
            composable("recuperarPassword") {

            }
            composable("loadingScreen") {

            }
            // En este navigation encapsulamos al usuario para que no pueda volver al login mientras usa la aplicación
            // de forma normal. Invocaremos al parámetro "route" cuando se haga un login exitoso.
            navigation(startDestination = "loadingScreen", route = "kirito") {
                composable("vistaHoy") {

                }
                composable("vistaMensual") {

                }
                composable("ajustesUsuario") {

                }
                composable("buscador") {

                }

            }
            //LoginScreen()

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
}