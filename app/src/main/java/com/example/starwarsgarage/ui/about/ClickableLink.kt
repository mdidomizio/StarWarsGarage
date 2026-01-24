package com.example.starwarsgarage.ui.about

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ClickableLink(
    @StringRes textRes: Int,
    @StringRes urlRes: Int,
    icon: Int,
    onUrlClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val url = stringResource(id = urlRes)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .clickable { onUrlClick(url) }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.White
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(id = textRes),
            fontSize = 16.sp,
            fontFamily = FontFamily.Default,
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { onUrlClick(url) }
        )
    }
}
