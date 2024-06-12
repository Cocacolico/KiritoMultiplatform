package es.kirito.kirito.menu.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import es.kirito.kirito.core.presentation.navigation.Graph
import es.kirito.kirito.menu.presentation.MenuScreen

fun NavGraphBuilder.menuPrincipalNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.MenuPrincipalGraph,
        startDestination = MenuPrincipalNav.MenuPrincipal.route
    ) {
        composable(route = MenuPrincipalNav.MenuPrincipal.route) {
            MenuScreen(navController)
        }
    }
}
sealed class MenuPrincipalNav(val route: String) {
    object MenuPrincipal : MenuPrincipalNav(route = "menuPrincipal")
    object Perfil : MenuPrincipalNav(route = "perfil")
    object MisCambios : MenuPrincipalNav(route = "misCambios")
    object DiasEspeciales : MenuPrincipalNav(route = "diasEspeciales")
    object TurnosNotas : MenuPrincipalNav(route = "turnosNotas")
    object TurnosCompaneros : MenuPrincipalNav(route = "turnosCompaneros")
    object ListinTelefonico : MenuPrincipalNav(route = "listinTelefonico")
    object PeticionsCambios : MenuPrincipalNav(route = "peticionesCambios")
    object TablonAnuncios : MenuPrincipalNav(route = "tablonAnuncios")
    object Graficos : MenuPrincipalNav(route = "graficos")
    object Notificaciones : MenuPrincipalNav(route = "notificaciones")
    object Estadisticas : MenuPrincipalNav(route = "estadisticas")
    object ExcesosJornada : MenuPrincipalNav(route = "excesosJornada")
    object SubirCuadroAnual : MenuPrincipalNav(route = "subirCuadro")
    object BorrarCuadro : MenuPrincipalNav(route = "borrarCuadro")
    object ConfigurarGCalendar : MenuPrincipalNav(route = "configurarGCalendar")
    object SubirMensual : MenuPrincipalNav(route = "subirMensual")
    object AdminSubirGrafico : MenuPrincipalNav(route = "subirGrafico")
    object AdminSubirLocalizadores : MenuPrincipalNav(route = "subirLocalizadores")
    object ContactarAdmin : MenuPrincipalNav(route = "contactarAdmin")
    object Mensajes : MenuPrincipalNav(route = "mensajes")
    object Ayuda : MenuPrincipalNav(route = "ayuda")
    object Ajustes : MenuPrincipalNav(route = "ajustes")
}