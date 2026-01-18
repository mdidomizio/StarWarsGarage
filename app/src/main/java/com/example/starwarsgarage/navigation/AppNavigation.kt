package com.example.starwarsgarage.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                sharedViewModel = sharedViewModel,
                navController = navController
            )
        }
        composable(Screen.Catalog.route) {
            StarshipsCatalogScreen(
                sharedViewModel = sharedViewModel,
                navController = navController
            )
        }
        composable(
            route = Screen.Pdp.route,
            arguments = listOf(navArgument("starshipId") { type = NavType.StringType })
        ) {
            StarshipPdpScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
        composable(Screen.Favorites.route) {
            StarshipsFavoritesScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
        composable(Screen.About.route) {
            AboutScreen(
                sharedViewModel = sharedViewModel
            )
        }
    }
}
