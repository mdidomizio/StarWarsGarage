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
import com.example.starwarsgarage.R
import com.example.starwarsgarage.ui.SharedViewModel
import com.example.starwarsgarage.ui.TopAppBarState
import com.example.starwarsgarage.ui.theme.starJediFontFamily

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(Unit) {
        sharedViewModel.updateTopAppBar(
            TopAppBarState(title = "", isVisible = false)
        )
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
        }
    }
}
