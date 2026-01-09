package com.example.starwarsgarage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.starwarsgarage.ui.SharedViewModel
import com.example.starwarsgarage.ui.about.AboutScreen
import com.example.starwarsgarage.ui.catalog.StarshipsCatalogScreen
import com.example.starwarsgarage.ui.favourites.StarshipsFavoritesScreen
import com.example.starwarsgarage.ui.home.HomeScreen
import com.example.starwarsgarage.ui.pdp.StarshipPdpScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME_SCREEN_ROUTE,
        modifier = modifier
    ) {
        navigation(
            startDestination = Screen.Home.route,
            route = AppDestinations.HOME_SCREEN_ROUTE
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navController = navController,
                    sharedViewModel = sharedViewModel
                )
            }
        }
        navigation(
            startDestination = Screen.Catalog.route,
            route = AppDestinations.CATALOG_SCREEN_ROUTE
        ) {
            composable(Screen.Catalog.route) { backStackEntry ->
                StarshipsCatalogScreen(
                    sharedViewModel = sharedViewModel,
                    navController = navController
                )
            }
            composable(Screen.Pdp.route) {
                StarshipPdpScreen(
                    navController = navController,
                    sharedViewModel = sharedViewModel
                )
            }
        }
        navigation(
            startDestination = Screen.Favorites.route,
            route = AppDestinations.FAVORITES_SCREEN_ROUTE
        ) {
            composable(Screen.Favorites.route) { backStackEntry ->
                StarshipsFavoritesScreen(
                    navController = navController,
                    sharedViewModel = sharedViewModel
                )
            }
        }
        navigation(
            startDestination = Screen.About.route,
            route = AppDestinations.ABOUT_SCREEN_ROUTE
        ) {
            composable(Screen.About.route) { backStackEntry ->
                AboutScreen(
                    navController = navController,
                    sharedViewModel = sharedViewModel
                )
            }
        }
    }
}
