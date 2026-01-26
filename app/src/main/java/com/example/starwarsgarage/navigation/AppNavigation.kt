package com.example.starwarsgarage.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.starwarsgarage.ui.viewmodel.SharedViewModel
import com.example.starwarsgarage.ui.about.AboutScreen
import com.example.starwarsgarage.ui.catalog.StarshipsCatalogScreen
import com.example.starwarsgarage.ui.favourites.StarshipsFavoritesScreen
import com.example.starwarsgarage.ui.home.HomeScreen
import com.example.starwarsgarage.ui.pdp.StarshipPdpScreen
import com.example.starwarsgarage.ui.viewmodel.FavoritesViewModel
import com.example.starwarsgarage.ui.viewmodel.HomeViewModel
import com.example.starwarsgarage.ui.viewmodel.StarshipPdpViewModel
import com.example.starwarsgarage.ui.viewmodel.StarshipsCatalogViewModel

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
        composable(Screen.Home.route) { backStackEntry ->
            val homeViewModel = hiltViewModel<HomeViewModel>(backStackEntry)
            HomeScreen(
                homeViewModel = homeViewModel,
                sharedViewModel = sharedViewModel,
                navController = navController
            )
        }
        composable(Screen.Catalog.route) { backStackEntry ->
            val catalogViewModel = hiltViewModel<StarshipsCatalogViewModel>(backStackEntry)
            StarshipsCatalogScreen(
                viewModel = catalogViewModel,
                sharedViewModel = sharedViewModel,
                navController = navController
            )
        }
        composable(
            route = Screen.Pdp.route,
            arguments = listOf(navArgument("starshipId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pdpViewModel = hiltViewModel<StarshipPdpViewModel>(backStackEntry)
            StarshipPdpScreen(
                viewModel = pdpViewModel,
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
        composable(Screen.Favorites.route) { backStackEntry ->
            val favoritesViewModel = hiltViewModel<FavoritesViewModel>(backStackEntry)
            StarshipsFavoritesScreen(
                navController = navController,
                viewModel = favoritesViewModel,
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
