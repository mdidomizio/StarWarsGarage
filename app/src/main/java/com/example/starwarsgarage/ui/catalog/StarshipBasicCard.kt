package com.example.starwarsgarage.ui.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.ui.pdp.StarshipImage
import com.example.starwarsgarage.ui.theme.starJediFontFamily

@Composable
fun StarshipBasicCard(
    starship: Starship,
    isFavorite: Boolean,
    onToggleFavourite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                StarshipImage(
                    starship = starship,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(
                            RoundedCornerShape(
                                topEnd = 0.dp,
                                topStart = 12.dp,
                                bottomEnd = 0.dp,
                                bottomStart = 12.dp
                            )
                        )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column (
                    modifier = Modifier.padding(16.dp)
                ){
                    starship.name?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = starJediFontFamily
                        )
                    }
                }
            }
            FavoriteIconButton(
                isFavorite = isFavorite,
                onToggleFavourite = onToggleFavourite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )
        }
    }
}
