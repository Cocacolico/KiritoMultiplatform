package es.kirito.kirito.turnos.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import es.kirito.kirito.core.presentation.navigation.Graph
import es.kirito.kirito.turnos.presentation.BuscadorScreen
import es.kirito.kirito.turnos.presentation.HoyScreen
import es.kirito.kirito.turnos.presentation.MensualScreen

fun NavGraphBuilder.turnosNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.TurnosNavGraph,
        startDestination = TurnosNav.TurnosHoy.route
    ) {
        composable(route = TurnosNav.TurnosHoy.route) {
            HoyScreen(navController)
        }
        composable(route = TurnosNav.TurnosMensual.route) {
            MensualScreen(navController)
        }
        composable(route = TurnosNav.TurnosBuscador.route) {
            BuscadorScreen(navController)
        }
    }
}

sealed class TurnosNav(val route: String) {
    object TurnosHoy : TurnosNav (route = "turnosHoy")
    object TurnosMensual : TurnosNav (route = "turnosMensual")
    object TurnosBuscador : TurnosNav (route = "turnosBuscador")
}