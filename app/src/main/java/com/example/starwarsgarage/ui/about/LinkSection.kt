package com.example.starwarsgarage.ui.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LinkSection(
    links: List<LinkData>,
    onUrlClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        links.forEach { link ->
            ClickableLink(
                textRes = link.textRes,
                urlRes = link.urlRes,
                icon = link.iconRes,
                onUrlClick = onUrlClick
            )
        }
    }
}
