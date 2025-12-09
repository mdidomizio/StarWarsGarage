package com.example.starwarsgarage.ui.favourites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton // Use M3 import
import androidx.compose.material3.MaterialTheme // Import MaterialTheme for colors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar // Use M3 import
import androidx.compose.material3.TopAppBarDefaults // Import for colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.starwarsgarage.R
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.navigation.AppDestinations
import com.example.starwarsgarage.ui.FavoritesViewModel
import com.example.starwarsgarage.ui.StarshipsViewModel
import com.example.starwarsgarage.ui.catalog.StarshipCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarshipsFavouritesScreen(
    favouritesViewModel: FavoritesViewModel = hiltViewModel(),
    starshipsViewModel: StarshipsViewModel = hiltViewModel(),
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val favouriteIds by favouritesViewModel.favouriteStarships.collectAsState()
    val allStarships = starshipsViewModel.starships.collectAsLazyPagingItems()
    var favouriteStarships by remember { mutableStateOf<List<Starship>> (emptyList()) }

    LaunchedEffect(favouriteIds, allStarships.itemSnapshotList) {
        val allItems = allStarships.itemSnapshotList.items
        favouriteStarships = allItems.filter { starship -> favouriteIds.contains(starship.id) }
    }
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
        if (favouriteStarships.isEmpty()) {
            EmptyFavouritesMessage(innerPadding)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(
                    items = favouriteStarships,
                    key = { starship -> starship.id }
                ) { starship ->
                    StarshipCard(
                        starship = starship,
                        isFavourite = true,
                        onClick = {
                            navController.navigate("${AppDestinations.PDP_SCREEN_ROUTE}/${starship.id}")
                        },
                        onFavouriteClick = { favouritesViewModel.toggleFavourite(starship.id)}
                    )
                }
            }
        }
    }
}

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