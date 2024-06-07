package es.kirito.kirito.menu.presentation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import es.kirito.kirito.menu.domain.BottomNavigationItems
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun KiritoBottomNavigation(
    navController: NavHostController,
    screens: List<BottomNavigationItems>
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    // Optimizar en el futuro
                    if(screen.icon != null)
                        Icon(
                        imageVector = screen.icon,
                        contentDescription = stringResource(resource = screen.label),
                            modifier = Modifier
                                .size(36.dp)
                    )
                    if(screen.drawable != null)
                        Icon(
                            imageVector = vectorResource(screen.drawable),
                            contentDescription = stringResource(resource = screen.label),
                            modifier = Modifier
                                .size(36.dp)
                        )
                       },
                label = { Text(stringResource(resource = screen.label)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().toString()) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}