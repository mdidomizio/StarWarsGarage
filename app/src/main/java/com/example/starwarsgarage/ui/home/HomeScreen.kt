package com.example.starwarsgarage.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.starwarsgarage.R
import com.example.starwarsgarage.navigation.Screen
import com.example.starwarsgarage.ui.HomeViewModel
import com.example.starwarsgarage.ui.SharedViewModel
import com.example.starwarsgarage.ui.TopAppBarState
import com.example.starwarsgarage.ui.theme.starJediFontFamily
import timber.log.Timber

@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val starship by homeViewModel.starshipOfTheDay.collectAsState()

    LaunchedEffect(Unit) {
        sharedViewModel.updateTopAppBar(
            TopAppBarState(title = "", isVisible = false)
        )
        homeViewModel.fetchStarshipOfTheDay()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.hyperspace_warp_blue),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .semantics{ contentDescription = "Decorative"},
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.home_screen_title),
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                fontFamily = starJediFontFamily,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 1f),
                        offset = Offset(9f, 9f),
                        blurRadius = 1f
                    )
                ),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.home_screen_subtitle),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontFamily = starJediFontFamily,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 1f),
                        offset = Offset(9f, 9f),
                        blurRadius = 1f
                    )
                ),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(32.dp))
            starship?.let{ starshipData ->
                StarshipShowstopper(
                    starship = starshipData,
                    onClick = {
                        Timber.tag("miriam").d("Navigating to PDP with starship: ${starshipData.id}")
                        navController.navigate(Screen.Pdp.createRoute(starshipData.id))
                    }
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            DriverShowstopper(
                onClick = {},
                modifier = modifier
            )
        }
    }
}
