package es.kirito.kirito.core.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import es.kirito.kirito.login.presentation.navigation.authNavGraph
import es.kirito.kirito.menu.navigation.menuPrincipalNavGraph
import es.kirito.kirito.turnos.presentation.navigation.turnosNavGraph

@Composable
fun RootNavigationGraph(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        route = Graph.RootNavGraph,
        startDestination = Graph.AuthNavGraph,
        modifier = Modifier.padding(innerPadding)
    ) {
        authNavGraph(navController = navController)
        menuPrincipalNavGraph(navController = navController)
        turnosNavGraph(navController = navController)
    }
}

object Graph {
    const val RootNavGraph = "root_graph"
    const val AuthNavGraph = "auth_graph"
    const val TurnosNavGraph = "turnos_graph"
    const val MenuPrincipalGraph = "menuPrincipal_graph"
}