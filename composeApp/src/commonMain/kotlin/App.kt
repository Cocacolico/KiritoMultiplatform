
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import es.kirito.kirito.core.presentation.theme.KiritoTheme
import es.kirito.kirito.login.presentation.LoginScreen
import es.kirito.kirito.login.presentation.RegisterScreen
import es.kirito.kirito.menu.presentation.MenuScreen
import es.kirito.kirito.precarga.presentation.PrecargaScreen
import es.kirito.kirito.turnos.presentation.BuscadorScreen
import es.kirito.kirito.turnos.presentation.HoyScreen
import es.kirito.kirito.turnos.presentation.MensualScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    KiritoTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") {
                LoginScreen(navController)
            }
            composable("register") {
                RegisterScreen(navController)
            }
            composable("recuperarPassword") {

            }
            composable("precarga") {
                PrecargaScreen(navController)
            }
            // En este navigation encapsulamos al usuario para que no pueda volver al login mientras usa la aplicación
            // de forma normal. Invocaremos al parámetro "route" cuando se haga un login exitoso.
            navigation(startDestination = "loadingScreen", route = "kirito") {
                composable("vistaHoy") {
                    HoyScreen(navController)
                }
                composable("vistaMensual") {
                    MensualScreen(navController)
                }
                composable("menuUsuario") {
                    MenuScreen(navController)
                }
                composable("buscador") {
                    BuscadorScreen(navController)
                }

            }

        }
    }
}