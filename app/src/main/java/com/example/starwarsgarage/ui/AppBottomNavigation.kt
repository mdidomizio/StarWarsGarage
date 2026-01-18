package com.example.starwarsgarage.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.starwarsgarage.R
import com.example.starwarsgarage.navigation.Screen
import com.example.starwarsgarage.ui.theme.starJediFontFamily

data class BottomNavItem(
    val label: String,
    val icon: Any,
    val route: String
)

@Composable
fun AppBottomNavigation(
    navController: NavController
) {
    val items = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Default.Home,
            route = Screen.Home.route
        ),
        BottomNavItem(
            label = "Catalog",
            icon = Icons.AutoMirrored.Filled.List,
            route = Screen.Catalog.route
        ),
        BottomNavItem(
            label = "My Garage",
            icon = painterResource(R.drawable.rocket_launch_64dp),
            route = Screen.Favorites.route
        ),
        BottomNavItem(
            label = "About",
            icon = Icons.Default.Settings,
            route = Screen.About.route
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentDestination?.route == item.route
            NavigationBarItem(
                icon = {
                    when (val icon = item.icon) {
                        is ImageVector -> Icon(imageVector = icon, contentDescription = item.label)
                        is Painter -> Icon(painter = icon, contentDescription = item.label)
                    }
                },
                label = { Text(text = item.label, fontFamily = starJediFontFamily) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
