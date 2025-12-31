package com.example.starwarsgarage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.starwarsgarage.navigation.AppDestinations.CATALOG_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.FAVORITES_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.GARAGE_GRAPH_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.HOME_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.PDP_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.STARSHIP_ID_KEY
import com.example.starwarsgarage.ui.home.HomeScreen
import com.example.starwarsgarage.ui.pdp.StarshipPdpScreen
import com.example.starwarsgarage.ui.catalog.StarshipsCatalogScreen
import com.example.starwarsgarage.ui.favourites.StarshipsFavoritesScreen
import com.example.starwarsgarage.ui.theme.StarWarsGarageTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarWarsGarageTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = HOME_SCREEN_ROUTE,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(HOME_SCREEN_ROUTE) {
                            HomeScreen(navController = navController)
                        }
                        navigation(
                            startDestination = CATALOG_SCREEN_ROUTE, // The first screen of this sub-graph
                            route = GARAGE_GRAPH_ROUTE
                        ) {
                            composable(CATALOG_SCREEN_ROUTE) { backStackEntry ->
                                val garageGraphEntry = remember(backStackEntry) {
                                    navController.getBackStackEntry(GARAGE_GRAPH_ROUTE)
                                }
                                StarshipsCatalogScreen(
                                    navController = navController,
                                    starshipsViewModel = hiltViewModel(garageGraphEntry),
                                    favoritesViewModel = hiltViewModel(garageGraphEntry)
                                )
                            }
                            composable(FAVORITES_SCREEN_ROUTE) { backStackEntry ->
                                val garageGraphEntry = remember(backStackEntry) {
                                    navController.getBackStackEntry(GARAGE_GRAPH_ROUTE)
                                }
                                StarshipsFavoritesScreen(
                                    navController = navController,
                                    favoritesViewModel = hiltViewModel(garageGraphEntry)
                                )
                            }
                        }

                        composable("${PDP_SCREEN_ROUTE}/{${STARSHIP_ID_KEY}}") {
                            StarshipPdpScreen(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
