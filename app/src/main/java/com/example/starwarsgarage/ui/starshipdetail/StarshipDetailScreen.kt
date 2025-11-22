package com.example.starwarsgarage.ui.starshipdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.navigation.NavHostController
import com.example.starwarsgarage.ui.StarshipsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarshipDetailScreen(viewModel: StarshipsViewModel, starshipId: String, navController: NavHostController, modifier: Modifier = Modifier) {
    LaunchedEffect(starshipId) {
        viewModel.getStarship(starshipId)
    }
    val starship by viewModel.starship.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = starship?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            starship?.let {
                Column {
                    Text(text = "Name: ${it.name}")
                    Text(text = "Model: ${it.model}")
                    Text(text = "Manufacturer: ${it.manufacturer}")
                    Text(text = "Cost: ${it.costInCredits}")
                }
            }
                ?: CircularProgressIndicator()
        }
    }
}