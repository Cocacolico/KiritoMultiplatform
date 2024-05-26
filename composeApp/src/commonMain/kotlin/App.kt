import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import es.kirito.kirito.core.presentation.theme.KiritoTheme
import es.kirito.kirito.core.presentation.theme.customColors
import es.kirito.kirito.login.presentation.LoginScreen
import es.kirito.kirito.login.presentation.RegisterScreen
import es.kirito.kirito.menu.domain.BottomNavigationItems
import es.kirito.kirito.menu.presentation.KiritoBottomNavigation
import es.kirito.kirito.menu.presentation.MenuScreen
import es.kirito.kirito.precarga.presentation.PrecargaScreen
import es.kirito.kirito.turnos.presentation.BuscadorScreen
import es.kirito.kirito.turnos.presentation.HoyScreen
import es.kirito.kirito.turnos.presentation.MensualScreen
import kirito.composeapp.generated.resources.Kirito_menu_menu_tittle
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.buscar
import kirito.composeapp.generated.resources.kirito_hoy_menu_tittle
import kirito.composeapp.generated.resources.mensual
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    KiritoTheme {
        val navController = rememberNavController()

        val bottomNavigationItems = listOf(
            BottomNavigationItems.Hoy,
            BottomNavigationItems.Mensual,
            BottomNavigationItems.Buscar,
            BottomNavigationItems.Mas
        )
        val destinosSinBarra = listOf(
            "login",
            "register",
            "recuperarPassword",
            "precarga"
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination
            Scaffold(
                bottomBar = {
                    if (currentDestination?.route !in destinosSinBarra)
                        KiritoBottomNavigation(navController, bottomNavigationItems)
                }
            ) {
                // Metemos el NavHost de navegación por las screens con la barra de navegación inferior en esta sección
                    innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "login",
                    Modifier.padding(innerPadding)
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
                    navigation(
                        startDestination = BottomNavigationItems.Hoy.route,
                        route = "kirito"
                    ) {
                        composable(BottomNavigationItems.Hoy.route) {
                            HoyScreen(navController)
                        }
                        composable(BottomNavigationItems.Mensual.route) {
                            MensualScreen(navController)
                        }
                        composable(BottomNavigationItems.Buscar.route) {
                            BuscadorScreen(navController)
                        }
                        composable(BottomNavigationItems.Mas.route) {
                            MenuScreen(navController)
                        }
                    }
                }
            }
        }

    }
}