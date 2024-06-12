package es.kirito.kirito.menu.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import es.kirito.kirito.menu.navigation.MenuPrincipalNav
import es.kirito.kirito.turnos.presentation.navigation.TurnosNav
import kirito.composeapp.generated.resources.Kirito_menu_menu_tittle
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.buscar
import kirito.composeapp.generated.resources.ic_japo_v3
import kirito.composeapp.generated.resources.kirito_hoy_menu_tittle
import kirito.composeapp.generated.resources.mensual
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource


sealed class BottomNavigationItems(
    val route: String,
    val icon: ImageVector? = null,
    val drawable: DrawableResource? = null,
    val label: StringResource
) {
    data object Hoy : BottomNavigationItems(route = TurnosNav.TurnosHoy.route, drawable = Res.drawable.ic_japo_v3, label = Res.string.kirito_hoy_menu_tittle)
    data object Mensual : BottomNavigationItems(route = TurnosNav.TurnosMensual.route, icon = Icons.Default.CalendarMonth, label = Res.string.mensual)
    data object Buscar : BottomNavigationItems(route = TurnosNav.TurnosBuscador.route, icon = Icons.Default.Search, label = Res.string.buscar)
    data object Mas : BottomNavigationItems(route = MenuPrincipalNav.MenuPrincipal.route, icon = Icons.Default.Menu, label = Res.string.Kirito_menu_menu_tittle)
}