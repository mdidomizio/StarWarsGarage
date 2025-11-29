package com.example.starwarsgarage.ui.starshipdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.starwarsgarage.R
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back_button_content_description))
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
                    Text(text = stringResource(id = R.string.error_fetching_starship_details))
                }
            }
            is StarshipDetailUiState.Success -> {
                LazyColumn(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color.Gray)
                        ) // Placeholder for image
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    item { StarshipProperty(label = stringResource(id = R.string.name_label), value = state.starship.name) }
                    item { StarshipProperty(label = stringResource(id = R.string.model_label), value = state.starship.model) }
                    item { StarshipProperty(label = stringResource(id = R.string.manufacturer_label), value = state.starship.manufacturer) }
                    item { StarshipProperty(label = stringResource(id = R.string.cost_label), value = state.starship.costInCredits) }
                    item { StarshipProperty(label = stringResource(id = R.string.length_label), value = state.starship.length) }
                    item { StarshipProperty(label = stringResource(id = R.string.max_atmosphering_speed_label), value = state.starship.maxAtmospheringSpeed) }
                    item { StarshipProperty(label = stringResource(id = R.string.crew_label), value = state.starship.crew) }
                    item { StarshipProperty(label = stringResource(id = R.string.passengers_label), value = state.starship.passengers) }
                    item { StarshipProperty(label = stringResource(id = R.string.cargo_capacity_label), value = state.starship.cargoCapacity) }
                    item { StarshipProperty(label = stringResource(id = R.string.consumables_label), value = state.starship.consumables) }
                    item { StarshipProperty(label = stringResource(id = R.string.hyperdrive_rating_label), value = state.starship.hyperdriveRating) }
                    item { StarshipProperty(label = stringResource(id = R.string.mglt_label), value = state.starship.mglt) }
                    item { StarshipProperty(label = stringResource(id = R.string.starship_class_label), value = state.starship.starshipClass) }
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
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = value, fontSize = 16.sp)
    }
    HorizontalDivider()
}
