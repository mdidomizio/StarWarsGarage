package com.example.starwarsgarage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.starwarsgarage.navigation.AppDestinations.CATALOG_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.FAVORITES_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.HOME_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.Screen
import com.example.starwarsgarage.ui.AppBottomNavigation
import com.example.starwarsgarage.ui.SharedViewModel
import com.example.starwarsgarage.ui.home.HomeScreen
import com.example.starwarsgarage.ui.pdp.StarshipPdpScreen
import com.example.starwarsgarage.ui.catalog.StarshipsCatalogScreen
import com.example.starwarsgarage.ui.favourites.StarshipsFavoritesScreen
import com.example.starwarsgarage.ui.theme.StarWarsGarageTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarWarsGarageTheme {
                val navController = rememberNavController()
                val sharedViewModel: SharedViewModel = hiltViewModel()
                val topAppBarState by sharedViewModel.topAppBarState.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(topAppBarState.title) },
                            navigationIcon = { topAppBarState.navigationIcon?.invoke() },
                            actions = { topAppBarState.actions?.invoke(this) }
                        )
                    },
                    bottomBar = { AppBottomNavigation(navController = navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = HOME_SCREEN_ROUTE,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        navigation(startDestination = Screen.Home.route, route = HOME_SCREEN_ROUTE) {
                            composable(Screen.Home.route) {
                                HomeScreen(
                                    navController = navController,
                                    sharedViewModel = sharedViewModel
                                )
                            }
                        }
                        navigation(startDestination = Screen.Catalog.route, route = CATALOG_SCREEN_ROUTE) {
                            composable(Screen.Catalog.route) { backStackEntry ->
                                StarshipsCatalogScreen(
                                    navController = navController,
                                    viewModel = hiltViewModel(),
                                    sharedViewModel = sharedViewModel
                                )
                            }
                            composable(Screen.Pdp.route) {
                                StarshipPdpScreen(
                                    navController = navController,
                                    sharedViewModel = sharedViewModel
                                )
                            }
                        }
                        navigation(startDestination = Screen.Favorites.route, route = FAVORITES_SCREEN_ROUTE) {
                            composable(Screen.Favorites.route) { backStackEntry ->
                                StarshipsFavoritesScreen(
                                    navController = navController,
                                    sharedViewModel = sharedViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
