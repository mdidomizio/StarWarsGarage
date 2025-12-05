package com.example.starwarsgarage.ui.pdp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.starwarsgarage.R
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.ui.ErrorScreen
import com.example.starwarsgarage.ui.StarshipsViewModel
import com.example.starwarsgarage.ui.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarshipPdpScreen(
    viewModel: StarshipsViewModel,
    starshipId: String,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(starshipId) {
        viewModel.getStarship(starshipId)
    }
    val uiState by viewModel.starshipUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = (uiState as? UiState.Success)?.starshipProduct?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_content_description)
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        when (val state = uiState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Error -> ErrorScreen(
                message = stringResource(id = R.string.error_fetching_starship_details),
                onRetry = { viewModel.retry(starshipId) }
            )

            is UiState.Success -> {
                val starship: Starship = state.starshipProduct
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    item {
                        StarshipImage(starship)
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    item {
                        StarshipExpandableDescriptionBlock(
                            label = stringResource(id = R.string.descriprion_label),
                            value = starship.description
                        )
                    }

                    if (starship.isPdpLoaded) {
                        val properties = listOf(
                            R.string.model_label to starship.model,
                            R.string.manufacturer_label to starship.manufacturer,
                            R.string.cost_label to starship.costInCredits,
                            R.string.length_label to starship.length,
                            R.string.max_atmosphering_speed_label to starship.maxAtmospheringSpeed,
                            R.string.cargo_capacity_label to starship.cargoCapacity,
                            R.string.consumables_label to starship.consumables,
                            R.string.hyperdrive_rating_label to starship.hyperdriveRating,
                            R.string.mglt_label to starship.mglt,
                            R.string.starship_class_label to starship.starshipClass,
                        )

                        item {
                            CrewAndPassengersInfo(
                                crew = starship.crew,
                                passengers = starship.passengers,
                            )
                        }

                        items(properties) { (labelRes, value) ->
                            StarshipProperty(
                                label = stringResource(id = labelRes),
                                value = value
                            )
                        }
                    }
                }
            }
        }
    }
}
