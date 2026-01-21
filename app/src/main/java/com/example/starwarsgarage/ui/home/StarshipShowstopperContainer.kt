package com.example.starwarsgarage.ui.home

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.ui.ShowstopperState
import com.example.starwarsgarage.R

@Composable
fun StarshipShowstopperContainer(
    state: ShowstopperState<Starship>,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    onStarshipClick: (String) -> Unit,
    modifier: Modifier = Modifier
){
    when {
        state.isLoading -> {
            CircularProgressIndicator(color = Color.White)
        }
        state.errorMessage != null -> {
            StarshipShowstopperErrorView(
                errorMessage = state.errorMessage,
                onRetry = onRetry,
                onDismiss = onDismiss
            )
        }
        state.item != null -> {
            StarshipShowstopper(
                starship = state.item,
                onClick = { onStarshipClick(state.item.id)}
            )
        }
        else -> {
            Text(
                text = stringResource(id = R.string.no_starship_available),
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}