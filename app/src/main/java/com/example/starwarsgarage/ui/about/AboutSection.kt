package com.example.starwarsgarage.ui.about

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.starwarsgarage.ui.theme.starJediFontFamily

@Composable
fun AboutSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.padding(bottom = 32.dp)
    ) {
        Card(
            modifier = Modifier.padding(horizontal = 16.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = starJediFontFamily,
                modifier = Modifier.padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 4.dp,
                    bottom = 4.dp
                )
            )
        }
        Spacer(modifier = modifier.height(4.dp))
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            content()
        }
    }
}
