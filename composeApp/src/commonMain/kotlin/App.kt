import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import es.kirito.kirito.core.presentation.navigation.RootNavigationGraph
import es.kirito.kirito.core.presentation.theme.KiritoTheme
import es.kirito.kirito.login.presentation.navigation.AuthNav
import es.kirito.kirito.menu.domain.BottomNavigationItems
import es.kirito.kirito.menu.navigation.MenuPrincipalNav
import es.kirito.kirito.menu.presentation.KiritoBottomNavigation
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
            AuthNav.Login.route,
            AuthNav.Register.route,
            AuthNav.RecuperarPassword.route,
            AuthNav.Precarga.route,
            MenuPrincipalNav.MenuPrincipal.route
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
            ) { innerPadding ->
                RootNavigationGraph(navController = navController, innerPadding = innerPadding)
            }
        }
    }
}