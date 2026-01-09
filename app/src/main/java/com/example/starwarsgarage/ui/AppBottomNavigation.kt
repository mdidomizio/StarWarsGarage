package com.example.starwarsgarage.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.starwarsgarage.navigation.AppDestinations.ABOUT_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.CATALOG_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.FAVORITES_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.HOME_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.Screen
import com.example.starwarsgarage.ui.theme.starJediFontFamily

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
        ),
        BottomNavItem(
            label = "About",
            icon = Icons.Default.Settings,
            route = Screen.About.route,
            baseRoute = ABOUT_SCREEN_ROUTE
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
       // windowInsets = WindowInsets(0.dp)
    ) {
        items.forEach { item ->
            val isSelected = currentDestination?.route == item.route
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(text = item.label, fontFamily = starJediFontFamily) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.baseRoute) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
