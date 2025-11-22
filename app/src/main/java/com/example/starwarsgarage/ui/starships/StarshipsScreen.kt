package com.example.starwarsgarage.ui.starships

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.starwarsgarage.R
import com.example.starwarsgarage.data.remote.Starship
import com.example.starwarsgarage.ui.StarshipsListUiState
import com.example.starwarsgarage.ui.StarshipsViewModel
import com.example.starwarsgarage.ui.theme.StarWarsGarageTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarshipsScreen(viewModel: StarshipsViewModel, navController: NavHostController, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.starship_catalog_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back_button_content_description))
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        when (val state = uiState) {
            is StarshipsListUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is StarshipsListUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(id = R.string.error_fetching_starships))
                }
            }
            is StarshipsListUiState.Success -> {
                val listState = rememberLazyListState()
                LazyColumn(modifier = Modifier.padding(innerPadding), state = listState) {
                    items(state.starships) { starship ->
                        StarshipCard(starship = starship, onClick = {
                            val id = starship.url.split("/").dropLast(1).last()
                            navController.navigate("starship_detail/$id")
                        })
                    }
                    if (state.isLoadingMore) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }

                val layoutInfo = listState.layoutInfo
                val visibleItemsInfo = layoutInfo.visibleItemsInfo
                if (visibleItemsInfo.isNotEmpty()) {
                    val lastVisibleItemIndex = visibleItemsInfo.last().index
                    if (lastVisibleItemIndex == state.starships.size - 1 && state.hasMore && !state.isLoadingMore) {
                        LaunchedEffect(lastVisibleItemIndex) {
                            viewModel.loadMoreStarships()
                        }
                    }
                }
            }
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
            Text(text = stringResource(id = R.string.name_label) + ": ${starship.name}")
            Text(text = stringResource(id = R.string.model_label) + ": ${starship.model}")
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
