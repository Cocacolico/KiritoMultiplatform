import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import es.kirito.kirito.core.presentation.theme.KiritoTheme
import es.kirito.kirito.login.presentation.LoginScreen
import es.kirito.kirito.login.presentation.RegisterScreen
import es.kirito.kirito.menu.domain.BottomNavigationItems
import es.kirito.kirito.menu.presentation.KiritoBottomNavigation
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
            val currentDestination =
                navController.currentBackStackEntryAsState().value?.destination
            Scaffold(
                bottomBar = {
                    if (currentDestination?.route !in destinosSinBarra)
                        KiritoBottomNavigation(navController, bottomNavigationItems)
                }
            ) {
                // Metemos el NavHost de navegación por las screens con la barra de navegación inferior en esta sección
                // Tenemos dos gráficos de navegación:
                // Auth para las screens de Login, Regitro, Recuperar Password y Precarga
                // Kirito para el resto de la aplicación

                    innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "auth",
                    enterTransition = {
                        EnterTransition.None
                    },
                    exitTransition = {
                        ExitTransition.None
                    },
                    modifier = Modifier.padding(innerPadding)
                ) {
                    navigation(
                        startDestination = "login",
                        route = "auth"
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
                            PrecargaScreen(
                                onNavigateToHoy = {
                                    navController.navigate("kirito") {
                                        popUpTo("auth") {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                    }
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