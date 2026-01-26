package com.example.starwarsgarage.ui.favourites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.starwarsgarage.R
import com.example.starwarsgarage.navigation.AppDestinations
import com.example.starwarsgarage.ui.ErrorScreen
import com.example.starwarsgarage.ui.viewmodel.FavoritesUiState
import com.example.starwarsgarage.ui.viewmodel.FavoritesViewModel
import com.example.starwarsgarage.ui.viewmodel.SharedViewModel
import com.example.starwarsgarage.ui.TopAppBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarshipsFavoritesScreen(
    navController: NavHostController,
    viewModel: FavoritesViewModel,
    sharedViewModel: SharedViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val screenTitle = stringResource(id = R.string.favorites_title)

    LaunchedEffect(Unit) {
        sharedViewModel.updateTopAppBar(
            TopAppBarState(
                title = screenTitle
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
                EmptyFavoritesMessage(PaddingValues())
            } else {
                FavoritesList(
                    starships = state.favoriteStarship,
                    innerPadding = PaddingValues(),
                    onToggleFavorite = { starship -> viewModel.toggleFavorite(starship) },
                    onStarshipClick = { starshipId ->
                        navController
                            .navigate("${AppDestinations.PDP_SCREEN_ROUTE}/$starshipId")
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
