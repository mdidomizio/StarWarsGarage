package com.example.starwarsgarage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.starwarsgarage.data.remote.Starship
import com.example.starwarsgarage.ui.StarshipsListUiState
import com.example.starwarsgarage.ui.theme.StarWarsGarageTheme
import com.example.starwarsgarage.ui.StarshipsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarWarsGarageTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = "starships_list", modifier = Modifier.padding(innerPadding)) {
                        composable("starships_list") {
                            StarshipsScreen(navController = navController)
                        }
                        composable("starship_detail/{starshipId}") { backStackEntry ->
                            val starshipId = backStackEntry.arguments?.getString("starshipId")
                            if (starshipId != null) {
                                StarshipDetailScreen(starshipId = starshipId, navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StarshipsScreen(modifier: Modifier = Modifier, viewModel: StarshipsViewModel = StarshipsViewModel(), navController: NavHostController) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is StarshipsListUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is StarshipsListUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error fetching starships")
            }
        }
        is StarshipsListUiState.Success -> {
            LazyColumn(modifier = modifier) {
                items(state.starships) { starship ->
                    StarshipCard(starship = starship, onClick = {
                        val id = starship.url.split("/").dropLast(1).last()
                        navController.navigate("starship_detail/$id")
                    })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarshipDetailScreen(modifier: Modifier = Modifier, viewModel: StarshipsViewModel = StarshipsViewModel(), starshipId: String, navController: NavHostController) {
    LaunchedEffect(starshipId) {
        viewModel.getStarship(starshipId)
    }
    val starship by viewModel.starship.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = starship?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            starship?.let {
                Column {
                    Text(text = "Name: ${it.name}")
                    Text(text = "Model: ${it.model}")
                    Text(text = "Manufacturer: ${it.manufacturer}")
                    Text(text = "Cost: ${it.costInCredits}")
                }
            }
                ?: CircularProgressIndicator()
        }
    }
}

@Composable
fun StarshipCard(starship: Starship, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${starship.name}")
            Text(text = "Model: ${starship.model}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StarshipsScreenPreview() {
    StarWarsGarageTheme {
        // Cannot preview screen with NavController
    }
}
