package com.example.starwarsgarage.ui.pdp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.starwarsgarage.R
import com.example.starwarsgarage.domain.model.Starship

@Composable
fun StarshipDescriptionBlock(
    label: String,
    value: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 16.sp
            )
        }
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
        Text(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp),
            text = value ?: "N/A",
            fontSize = 16.sp,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Justify
        )
    }
    HorizontalDivider()
}
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
        val imageRequest = ImageRequest.Builder(context)
            .data(starship.image)
            .crossfade(enable = true)
            .build()

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

@Composable
fun StarshipProperty(
    label: String,
    value: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 16.sp)
        Text(
            text = value ?: "N/A",
            fontSize = 16.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
        )
    }
    HorizontalDivider()
}

@Composable
fun StarshipExpandableDescriptionBlock(
    label: String,
    value: String?,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 16.sp
            )
            Icon(
                imageVector =
                if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Collapse" else "Expand"
            )
        }
        HorizontalDivider()
        AnimatedVisibility(visible = expanded) {
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                text = value ?: "N/A",
                fontSize = 16.sp,
                fontFamily = FontFamily.Default,
                textAlign = TextAlign.Justify
            )
        }
    }
    HorizontalDivider()
}

@Composable
fun CrewAndPassengersInfo(
    crew: String?,
    passengers: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InfoCard(
            modifier = Modifier.weight(1f),
            title = stringResource(id = R.string.crew_label),
            value = crew
        )
        InfoCard(
            modifier = Modifier.weight(1f),
            title = stringResource(id = R.string.passengers_label),
            value = passengers
        )
    }
    HorizontalDivider()
}

@Composable
fun InfoCard(
    title: String,
    value: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = value ?: "N/A",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
