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
import com.example.starwarsgarage.ui.StarshipsViewModel
import com.example.starwarsgarage.ui.starshipdetail.StarshipDetailScreen
import com.example.starwarsgarage.ui.starships.StarshipsScreen
import com.example.starwarsgarage.ui.theme.StarWarsGarageTheme

class MainActivity : ComponentActivity() {

    private val viewModel: StarshipsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarWarsGarageTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = "starships_list", modifier = Modifier.padding(innerPadding)) {
                        composable("starships_list") {
                            StarshipsScreen(viewModel = viewModel, navController = navController)
                        }
                        composable("starship_detail/{starshipId}") { backStackEntry ->
                            val starshipId = backStackEntry.arguments?.getString("starshipId")
                            if (starshipId != null) {
                                StarshipDetailScreen(viewModel = viewModel, starshipId = starshipId, navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}
