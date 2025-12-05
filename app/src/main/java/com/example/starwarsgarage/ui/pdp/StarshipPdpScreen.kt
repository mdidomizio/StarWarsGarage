package com.example.starwarsgarage.ui.pdp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.starwarsgarage.R
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.ui.ErrorScreen
import com.example.starwarsgarage.ui.UiState
import com.example.starwarsgarage.ui.StarshipsViewModel
import com.example.starwarsgarage.ui.theme.starJediFontFamily

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
                ) {item {
                    Box {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .zIndex(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                        val context = LocalContext.current
                        val imageRequest = ImageRequest.Builder(context)
                            .data(starship.image)
                            .crossfade(enable = true)
                            .build()

                        AsyncImage(
                            model = imageRequest,
                            contentDescription = stringResource(
                                id = R.string.starship_image_description,
                                starship.name ?: ""
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .zIndex(2f),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    item {
                        Text(
                            text = starship.description ?: stringResource(id = R.string.no_description_available),
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Default,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        HorizontalDivider()
                    }
                    item {
                        StarshipProperty(
                            label = stringResource(id = R.string.model_label),
                            value = starship.model
                        )
                    }
                    item {
                        StarshipProperty(
                            label = stringResource(id = R.string.manufacturer_label),
                            value = starship.manufacturer
                        )
                    }
                    item {
                        StarshipProperty(
                            label = stringResource(id = R.string.cost_label),
                            value = starship.costInCredits
                        )
                    }
                    item {
                        StarshipProperty(
                            label = stringResource(id = R.string.length_label),
                            value = starship.length
                        )
                    }
                    item {
                        StarshipProperty(
                            label = stringResource(id = R.string.max_atmosphering_speed_label),
                            value = starship.maxAtmospheringSpeed
                        )
                    }
                    item {
                        Column {
                            CrewAndPassengersGrid(
                                crew = starship.crew,
                                passengers = starship.passengers,
                            )
                            StarshipProperty(
                                label = stringResource(id = R.string.cargo_capacity_label),
                                value = starship.cargoCapacity
                            )
                            StarshipProperty(
                                label = stringResource(id = R.string.consumables_label),
                                value = starship.consumables
                            )
                            StarshipProperty(
                                label = stringResource(id = R.string.hyperdrive_rating_label),
                                value = starship.hyperdriveRating
                            )
                            StarshipProperty(
                                label = stringResource(id = R.string.mglt_label),
                                value = starship.mglt
                            )
                            StarshipProperty(
                                label = stringResource(id = R.string.starship_class_label),
                                value = starship.starshipClass
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StarshipProperty(label: String, value: String?, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 16.sp)
        Text(
            text = value ?: "N/A",
            fontSize = 16.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold
        )
    }
    HorizontalDivider()
}

@Composable
fun CrewAndPassengersGrid(
    crew: String?,
    passengers: String?,
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        stringResource(id = R.string.crew_label) to crew,
        stringResource(id = R.string.passengers_label) to passengers
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = false
    ) {
        items(items.size) { index ->
            val (title, value) = items[index]
            InfoCard(
                title = title,
                value = value
            )
        }
    }
}

@Composable
fun InfoCard(title: String, value: String?) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            maxLines = 1,
            fontFamily = starJediFontFamily,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = value ?: "N/A",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        HorizontalDivider()
    }
}
