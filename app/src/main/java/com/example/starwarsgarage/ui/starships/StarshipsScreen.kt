package com.example.starwarsgarage.ui.starships

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.starwarsgarage.data.remote.Starship
import com.example.starwarsgarage.ui.StarshipsListUiState
import com.example.starwarsgarage.ui.StarshipsViewModel
import com.example.starwarsgarage.ui.theme.StarWarsGarageTheme

@Composable
fun StarshipsScreen(modifier: Modifier = Modifier, viewModel: StarshipsViewModel = StarshipsViewModel(), navController: NavHostController) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is StarshipsListUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is StarshipsListUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error fetching starships")
            }
        }
        is StarshipsListUiState.Success -> {
            LazyColumn(modifier = modifier) {
                items(state.starships) { starship ->
                    StarshipCard(starship = starship, onClick = {
                        val id = starship.url.split("/").dropLast(1).last()
                        navController.navigate("starship_detail/$id")
                    })
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
            Text(text = "Name: ${starship.name}")
            Text(text = "Model: ${starship.model}")
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
