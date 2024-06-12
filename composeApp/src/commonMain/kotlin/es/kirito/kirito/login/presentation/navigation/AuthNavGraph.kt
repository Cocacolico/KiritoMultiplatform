package es.kirito.kirito.login.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import es.kirito.kirito.core.presentation.navigation.Graph
import es.kirito.kirito.login.presentation.LoginScreen
import es.kirito.kirito.login.presentation.RegisterScreen
import es.kirito.kirito.precarga.presentation.PrecargaScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AuthNavGraph,
        startDestination = AuthNav.Login.route
    ) {
        composable(AuthNav.Login.route) {
            LoginScreen(navController)
        }
        composable(AuthNav.Register.route) {
            RegisterScreen(navController)
        }
        composable(AuthNav.RecuperarPassword.route) {

        }
        composable(AuthNav.Precarga.route) {
            PrecargaScreen(
                onNavigateToHoy = {
                    navController.navigate(Graph.TurnosNavGraph) {
                        popUpTo(Graph.AuthNavGraph) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

sealed class AuthNav(val route: String) {
    object Login : AuthNav(route = "login")
    object Register : AuthNav(route = "register")
    object RecuperarPassword : AuthNav(route = "recuperarPassword")
    object Precarga : AuthNav(route = "precarga")
}