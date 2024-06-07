package es.kirito.kirito.menu.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import kirito.composeapp.generated.resources.Kirito_menu_menu_tittle
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.buscar
import kirito.composeapp.generated.resources.ic_japo_v3
import kirito.composeapp.generated.resources.kirito_hoy_menu_tittle
import kirito.composeapp.generated.resources.mensual
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.vectorResource


sealed class BottomNavigationItems(
    val route: String,
    val icon: ImageVector? = null,
    val drawable: DrawableResource? = null,
    val label: StringResource
) {
    //Icons.Default.CalendarToday
    data object Hoy : BottomNavigationItems(route = "vistaHoy", drawable = Res.drawable.ic_japo_v3, label = Res.string.kirito_hoy_menu_tittle)
    data object Mensual : BottomNavigationItems(route = "vistaMensual", icon = Icons.Default.CalendarMonth, label = Res.string.mensual)
    data object Buscar : BottomNavigationItems(route = "buscador", icon = Icons.Default.Search, label = Res.string.buscar)
    data object Mas : BottomNavigationItems(route = "menuUsuario", icon = Icons.Default.Menu, label = Res.string.Kirito_menu_menu_tittle)
}