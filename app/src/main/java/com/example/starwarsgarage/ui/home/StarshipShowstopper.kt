package com.example.starwarsgarage.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.starwarsgarage.R
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.ui.theme.starJediFontFamily

@Composable
fun StarshipShowstopper(
    //starship: Starship,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.udm27h95f7l51),
                contentDescription = stringResource(
                    id = R.string.starship_image_description,
                    "temporary name"
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(150.dp)
            )
            /*AsyncImage(
                model = starship.url,
                contentDescription = stringResource(
                    id = R.string.starship_image_description,
                    starship.name ?: ""
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(100.dp)
            )*/

            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = "Starship of the Day:",
                    fontFamily = starJediFontFamily,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                //starship.name?.let {
                    Text(
                        text = "starship name", //it,
                        fontSize = 20.sp,
                        fontFamily = starJediFontFamily,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                    )
                //}
            }
        }
    }
}
