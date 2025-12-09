package com.example.starwarsgarage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.starwarsgarage.navigation.AppDestinations.CATALOG_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.FAVOURITES_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.HOME_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.PDP_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.STARSHIP_ID_KEY
import com.example.starwarsgarage.ui.StarshipsViewModel
import com.example.starwarsgarage.ui.home.HomeScreen
import com.example.starwarsgarage.ui.pdp.StarshipPdpScreen
import com.example.starwarsgarage.ui.catalog.StarshipsCatalogScreen
import com.example.starwarsgarage.ui.favourites.StarshipsFavouritesScreen
import com.example.starwarsgarage.ui.theme.StarWarsGarageTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: StarshipsViewModel by viewModels()

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
                        composable(CATALOG_SCREEN_ROUTE) {
                            StarshipsCatalogScreen(starshipsViewModel = viewModel, navController = navController)
                        }
                        composable("${PDP_SCREEN_ROUTE}/{${STARSHIP_ID_KEY}}") { backStackEntry ->
                            val starshipId = backStackEntry.arguments?.getString(STARSHIP_ID_KEY)
                            if (starshipId != null) {
                                StarshipPdpScreen(
                                    viewModel = viewModel,
                                    starshipId = starshipId,
                                    navController = navController
                                )
                            }
                        }
                        composable(FAVOURITES_SCREEN_ROUTE) {
                            StarshipsFavouritesScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
