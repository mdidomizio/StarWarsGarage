package com.example.starwarsgarage.ui.pdp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.starwarsgarage.R
import com.example.starwarsgarage.domain.model.Starship

@Composable
fun StarshipImage(
    starship: Starship,
    modifier: Modifier
) {
    Box (
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .matchParentSize()
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        val context = LocalContext.current
        val imageRequest = remember(starship.image) {
            ImageRequest.Builder(context)
                .data(starship.image)
                .size (500, 500)
                .crossfade(enable = true)
                .memoryCacheKey(starship.image)
                .diskCacheKey(starship.image)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()
        }

        AsyncImage(
            model = imageRequest,
            contentDescription = stringResource(
                id = R.string.starship_image_description,
                starship.name ?: ""
            ),
            modifier = Modifier
                .matchParentSize()
                .zIndex(2f),
            contentScale = ContentScale.Crop
        )
    }
}
