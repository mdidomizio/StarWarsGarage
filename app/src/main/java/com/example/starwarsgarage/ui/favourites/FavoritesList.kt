package com.example.starwarsgarage.ui.favourites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.starwarsgarage.domain.model.STARSHIP_ID_MAP
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.ui.catalog.StarshipBasicCard
import com.example.starwarsgarage.ui.catalog.StarshipExtendedCard

@Composable
fun FavoritesList(
    starships: List<Starship>,
    innerPadding: PaddingValues,
    onToggleFavorite: (Starship) -> Unit,
    onStarshipClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        items(
            items = starships,
            key = { starship -> starship.id }
        ) { starship ->
            val hasExtendedDetails =
                starship.name != null && STARSHIP_ID_MAP.contains(starship.name)
            if (hasExtendedDetails) {
                StarshipExtendedCard(
                    starship = starship,
                    isFavorite = true,
                    onToggleFavourite = { onToggleFavorite(starship) },
                    onClick = { onStarshipClick(starship.id) }
                )
            } else {
                StarshipBasicCard(
                    starship = starship,
                    isFavorite = true,
                    onToggleFavourite = { onToggleFavorite(starship) },
                    onClick = { onStarshipClick(starship.id) }
                )
            }
        }
    }
}
