package com.example.starwarsgarage.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.starwarsgarage.navigation.AppDestinations.CATALOG_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.FAVORITES_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.HOME_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.Screen
import timber.log.Timber

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
    val baseRoute: String
)

@Composable
fun AppBottomNavigation(
    navController: NavController
) {
    val items = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Default.Home,
            route = Screen.Home.route,
            baseRoute = HOME_SCREEN_ROUTE
        ),
        BottomNavItem(
            label = "Catalog",
            icon = Icons.AutoMirrored.Filled.List,
            route = Screen.Catalog.route,
            baseRoute = CATALOG_SCREEN_ROUTE
        ),
        BottomNavItem(
            label = "Favorites",
            icon = Icons.Default.Favorite,
            route = Screen.Favorites.route,
            baseRoute = FAVORITES_SCREEN_ROUTE
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Timber.tag("miriam").d("Current destination: ${currentDestination?.route}")

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == item.baseRoute } == true
            Timber.tag("miriam").d("Item: ${item.label}, route: ${item.route}, isSelected: $isSelected")
            NavigationBarItem(
                icon = { Icon(
                    imageVector = item.icon,
                    contentDescription = item.label
                )
                       },
                label = { Text(text = item.label) },
                selected = isSelected,
                onClick = {
                    Timber.tag("miriam").d("Clicked on: ${item.label}, currentDestination: ${currentDestination?.route}, item.baseRoute: ${item.baseRoute}")
                    navController.navigate(item.baseRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
