package com.example.starwarsgarage.ui.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun StarshipExtendedCard(
    starship: Starship,
    isFavorite: Boolean,
    onToggleFavourite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = MaterialTheme.shapes.medium
    val cornerSize = 12.dp //default corner size for MaterialTheme.shapes.medium

    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = cardShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column {
            StarshipImage(
                starship,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(
                        RoundedCornerShape(
                            topEnd = cornerSize,
                            topStart = cornerSize,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp,
                        )
                    )
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    starship.name?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = starJediFontFamily
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    FavoriteIconButton(
                        isFavorite = isFavorite,
                        onToggleFavourite = onToggleFavourite,
                    )
                }
            }

        }

    }
}
