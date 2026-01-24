package com.example.starwarsgarage.ui.catalog

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.starwarsgarage.R

@Composable
fun FavoriteIconButton(
    isFavorite: Boolean,
    onToggleFavourite: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onToggleFavourite,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.rocket_launch_64dp),
            contentDescription =stringResource(id = R.string.favorite_icon_content_description),
            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}
