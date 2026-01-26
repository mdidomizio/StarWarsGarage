package com.example.starwarsgarage.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.starwarsgarage.ui.theme.starJediFontFamily

@Composable
fun DriverShowstopper(
    // driver: Driver,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseShowstopper(
        modifier = modifier,
        title = "Driver of the Day:"/*stringResource(id = R.string.showstopper_starship_title)*/,
        imageContent = {
            BaseImage(
                imageUrl = "https://lumiere-a.akamaihd.net/v1/images/han-solo-main_a4c8ff79.jpeg",
                contentDescription =  "Han Solo",
                modifier = Modifier
                    .size(150.dp)
            )
        },
        textContent = {
            Text(
                text = "Han Solo",
                fontSize = 20.sp,
                fontFamily = starJediFontFamily,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            )
        },
        onClick = onClick
    )
}

@Composable
fun BaseImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .matchParentSize()
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
    }
        val context = LocalContext.current
        val imageRequest = remember(imageUrl) {
            ImageRequest.Builder(context)
                .data(imageUrl)
                .size(500, 500)
                .crossfade(enable = true)
                .memoryCacheKey(imageUrl)
                .diskCacheKey(imageUrl)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()
        }
        AsyncImage(
            model = imageRequest,
            contentDescription = contentDescription /*stringResource(id = R.string.item_image_description, item.name ?: "")*/,
            modifier = Modifier
                .matchParentSize()
                .zIndex(2f),
            contentScale = ContentScale.Crop
        )
    }
}
