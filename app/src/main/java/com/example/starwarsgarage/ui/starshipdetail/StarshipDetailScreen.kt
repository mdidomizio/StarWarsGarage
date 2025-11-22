package com.example.starwarsgarage.ui.starshipdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.starwarsgarage.data.remote.Starship
import com.example.starwarsgarage.ui.StarshipDetailUiState
import com.example.starwarsgarage.ui.StarshipsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarshipDetailScreen(viewModel: StarshipsViewModel, starshipId: String, navController: NavHostController, modifier: Modifier = Modifier) {
    LaunchedEffect(starshipId) {
        viewModel.getStarship(starshipId)
    }
    val uiState by viewModel.starshipUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = (uiState as? StarshipDetailUiState.Success)?.starship?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        when (val state = uiState) {
            is StarshipDetailUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is StarshipDetailUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    Text(text = "Error fetching starship details")
                }
            }
            is StarshipDetailUiState.Success -> {
                Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                    StarshipProperty(label = "Name", value = state.starship.name)
                    StarshipProperty(label = "Model", value = state.starship.model)
                    StarshipProperty(label = "Manufacturer", value = state.starship.manufacturer)
                    StarshipProperty(label = "Cost", value = state.starship.costInCredits)
                    StarshipProperty(label = "Length", value = state.starship.length)
                    StarshipProperty(label = "Max Atmosphering Speed", value = state.starship.maxAtmospheringSpeed)
                    StarshipProperty(label = "Crew", value = state.starship.crew)
                    StarshipProperty(label = "Passengers", value = state.starship.passengers)
                    StarshipProperty(label = "Cargo Capacity", value = state.starship.cargoCapacity)
                    StarshipProperty(label = "Consumables", value = state.starship.consumables)
                    StarshipProperty(label = "Hyperdrive Rating", value = state.starship.hyperdriveRating)
                    StarshipProperty(label = "MGLT", value = state.starship.mglt)
                    StarshipProperty(label = "Starship Class", value = state.starship.starshipClass)
                }
            }
        }
    }
}

@Composable
fun StarshipProperty(label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(text = value)
    }
    Divider()
}