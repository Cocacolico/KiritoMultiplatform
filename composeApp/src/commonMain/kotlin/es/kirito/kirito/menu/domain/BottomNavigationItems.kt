package es.kirito.kirito.menu.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import kirito.composeapp.generated.resources.Kirito_menu_menu_tittle
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.buscar
import kirito.composeapp.generated.resources.kirito_hoy_menu_tittle
import kirito.composeapp.generated.resources.mensual
import org.jetbrains.compose.resources.StringResource

sealed class BottomNavigationItems(
    val route: String,
    val icon: ImageVector,
    val label: StringResource
) {
    data object Hoy : BottomNavigationItems("vistaHoy", Icons.Default.CalendarToday, Res.string.kirito_hoy_menu_tittle) //Image(painterResource(Res.drawable.ic_japo_v3),null)
    data object Mensual : BottomNavigationItems("vistaMensual", Icons.Default.CalendarMonth, Res.string.mensual)
    data object Buscar : BottomNavigationItems("buscador", Icons.Default.Search, Res.string.buscar)
    data object Mas : BottomNavigationItems("menuUsuario", Icons.Default.Menu, Res.string.Kirito_menu_menu_tittle)
}