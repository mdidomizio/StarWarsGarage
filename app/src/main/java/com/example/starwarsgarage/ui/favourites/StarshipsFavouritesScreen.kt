package com.example.starwarsgarage.ui.favourites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.starwarsgarage.R
import com.example.starwarsgarage.domain.model.STARSHIP_ID_MAP
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.navigation.AppDestinations
import com.example.starwarsgarage.ui.ErrorScreen
import com.example.starwarsgarage.ui.FavoritesUiState
import com.example.starwarsgarage.ui.FavoritesViewModel
import com.example.starwarsgarage.ui.SharedViewModel
import com.example.starwarsgarage.ui.TopAppBarState
import com.example.starwarsgarage.ui.catalog.StarshipBasicCard
import com.example.starwarsgarage.ui.catalog.StarshipExtendedCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarshipsFavoritesScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val screenTitle = stringResource(id = R.string.favorites_title)

    LaunchedEffect(Unit) {
        sharedViewModel.updateTopAppBar(
            TopAppBarState(
                title = screenTitle,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_content_description)
                        )
                    }
                }
            )
        )
    }

    when (val state = uiState) {
        is FavoritesUiState.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is FavoritesUiState.Success -> {
            if (state.favoriteStarship.isEmpty()) {
                EmptyFavouritesMessage(PaddingValues())
            } else {
                FavoritesList(
                    starships = state.favoriteStarship,
                    innerPadding = PaddingValues(),
                    onToggleFavorite = { starship -> viewModel.toggleFavorite(starship) },
                    onStarshipClick = { starshipId ->
                        navController.navigate("${AppDestinations.PDP_SCREEN_ROUTE}/$starshipId")
                    }
                )
            }
        }

        is FavoritesUiState.Error -> {
            ErrorScreen(
                message = state.message
                    ?: stringResource(id = R.string.error_fetching_starships),
                onRetry = { viewModel.onRetry() }
            )
        }
    }
}

@Composable
fun FavoritesList(
    starships: List<Starship>,
    innerPadding: PaddingValues,
    onToggleFavorite: (Starship) -> Unit,
    onStarshipClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        items(
            items = starships,
            key = { starship -> starship.id }
        ) { starship ->
            val hasExtendedDetails =
                starship.name != null && STARSHIP_ID_MAP.contains(starship.name)
            if (hasExtendedDetails) {
                StarshipExtendedCard(
                    starship = starship,
                    isFavorite = true,
                    onToggleFavourite = { onToggleFavorite(starship) },
                    onClick = { onStarshipClick(starship.id) }
                )
            } else {
                StarshipBasicCard(
                    starship = starship,
                    isFavorite = true,
                    onToggleFavourite = { onToggleFavorite(starship) },
                    onClick = { onStarshipClick(starship.id) }
                )
            }
        }
    }
}

@Composable
fun EmptyFavouritesMessage(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.no_favorites_yet),
            textAlign = TextAlign.Center
        )
    }
}
