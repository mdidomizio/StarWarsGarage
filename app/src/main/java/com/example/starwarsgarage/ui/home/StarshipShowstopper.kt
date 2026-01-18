package com.example.starwarsgarage.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.starwarsgarage.R
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.ui.pdp.StarshipImage
import com.example.starwarsgarage.ui.theme.starJediFontFamily

@Composable
fun StarshipShowstopper(
    starship: Starship,
    modifier: Modifier = Modifier
) {
    BaseShowstopper (
        modifier = modifier,
        title = stringResource(id = R.string.showstopper_starship_title),
        imageContent = {
            StarshipImage(
                starship = starship,
                modifier = Modifier
                    .size(150.dp)
            )
        },
       textContent = {
            starship.name?.let {
                Text(
                    text = it,
                    fontSize = 20.sp,
                    fontFamily = starJediFontFamily,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    /*modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )*/
                )
            }
        }
    )
}
