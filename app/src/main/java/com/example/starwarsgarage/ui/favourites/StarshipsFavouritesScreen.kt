package com.example.starwarsgarage.ui.favourites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.starwarsgarage.R
import com.example.starwarsgarage.navigation.AppDestinations
import com.example.starwarsgarage.ui.FavoritesViewModel
import com.example.starwarsgarage.ui.catalog.StarshipCard
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarshipsFavouritesScreen(
    favouritesViewModel: FavoritesViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val favouriteStarships = favouritesViewModel.favoriteStarships.collectAsLazyPagingItems()
    Timber.tag("Miriam").d("favouriteStarships.loadState.refresh: %s", favouriteStarships.loadState.refresh)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.favorites_title)) },
                navigationIcon = {
                    IconButton (onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_content_description)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        when (val refreshState = favouriteStarships.loadState.refresh) {
            is LoadState.Loading -> {
                Timber.tag("Miriam").d("State is Loading")
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.Error -> {
                Timber.tag("Miriam").e(refreshState.error, "State is Error")
                EmptyFavouritesMessage(innerPadding)
            }

            is LoadState.NotLoading -> {
                if (favouriteStarships.itemCount == 0) {
                    Timber.tag("Miriam").d("State is NotLoading, but itemCount is 0")
                    EmptyFavouritesMessage(innerPadding)
                } else {
                    Timber.tag("Miriam").d("State is NotLoading, itemCount: %d", favouriteStarships.itemCount)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        items(
                            count = favouriteStarships.itemCount,
                            key = { index -> favouriteStarships.peek(index)?.id ?: "" }
                        ) { index ->
                            val starship = favouriteStarships[index]
                            if (starship != null) {
                                StarshipCard(
                                    starship = starship,
                                    isFavourite = true,
                                    onToggleFavourite = {
                                        Timber.tag("Miriam").d("Toggling favorite for starship ID from favorites screen: %s", starship.id)
                                        favouritesViewModel.onToggleFavorite(starship.id) },
                                    onClick = {
                                        navController.navigate("${AppDestinations.PDP_SCREEN_ROUTE}/${starship.id}")
                                    }
                                )
                            } else {
                                Timber.tag("Miriam").d("Starship at index %d is null", index)
                            }
                        }
                    }
                }
            }
        }
    }}

@Composable
fun EmptyFavouritesMessage( innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = stringResource(id = R.string.no_favorites_yet),
            textAlign = TextAlign.Center
        )
    }
}
