package com.example.starwarsgarage.ui.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.starwarsgarage.R
import com.example.starwarsgarage.domain.model.STARSHIP_ID_MAP
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.navigation.AppDestinations.PDP_SCREEN_ROUTE
import com.example.starwarsgarage.ui.ErrorScreen
import com.example.starwarsgarage.ui.viewmodel.SharedViewModel
import com.example.starwarsgarage.ui.viewmodel.StarshipsCatalogViewModel
import com.example.starwarsgarage.ui.TopAppBarState
import com.example.starwarsgarage.ui.pdp.StarshipImage
import com.example.starwarsgarage.ui.theme.starJediFontFamily

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun StarshipsCatalogScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: StarshipsCatalogViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyPagingItems: LazyPagingItems<Starship> = viewModel.starships.collectAsLazyPagingItems()
    val screenTitle = stringResource(id = R.string.starship_catalog_title)

    LaunchedEffect(Unit) {
        sharedViewModel.updateTopAppBar(
            TopAppBarState(
                title = screenTitle
            )
        )
    }

    val isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading
    val pullRefreshState = rememberPullRefreshState(
        isRefreshing,
        onRefresh = { lazyPagingItems.refresh() }
    )

    Box(
        modifier = modifier
            .pullRefresh(pullRefreshState)
    ) {
        if (lazyPagingItems.loadState.refresh is LoadState.Error && lazyPagingItems.itemCount == 0) {
            ErrorScreen(
                onRetry = { lazyPagingItems.retry() },
                message = stringResource(id = R.string.error_fetching_starships)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = lazyPagingItems.itemCount,
                    key = lazyPagingItems.itemKey { it.id }
                ) { index ->
                    val starship = lazyPagingItems[index]
                    if (starship != null) {
                        //stale data from Starship Object (starship.isFavorite)-> bad
                        // latest set of favorite ids (uiState.isFavorite) -> good for recomposition
                        val isFavorite = uiState.favoriteIds.contains(starship.id)
                        val hasExtendedDetails =
                            starship.name != null && STARSHIP_ID_MAP.contains(starship.name)

                        if (hasExtendedDetails) {
                            StarshipExtendedCard(
                                starship = starship,
                                isFavorite = isFavorite,
                                onToggleFavourite = { viewModel.onFavoriteToggled(starship) },
                                onClick = {
                                    navController.navigate("${PDP_SCREEN_ROUTE}/${starship.id}")
                                }
                            )
                        } else {
                            StarshipBasicCard(
                                starship = starship,
                                isFavorite = isFavorite,
                                onToggleFavourite = { viewModel.onFavoriteToggled(starship) },
                                onClick = {
                                    navController.navigate("${PDP_SCREEN_ROUTE}/${starship.id}")
                                }
                            )
                        }
                    }
                }

                handleLoadStates(lazyPagingItems)
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

private fun LazyListScope.handleLoadStates(lazyPagingItems: LazyPagingItems<Starship>) {
    lazyPagingItems.loadState.append.let {
        when (it) {
            is LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            is LoadState.Error -> {
                item {
                    ErrorScreen(
                        message = stringResource(id = R.string.error_fetching_starships),
                        onRetry = { lazyPagingItems.retry() }
                    )
                }
            }
            else -> {}
        }
    }
}
