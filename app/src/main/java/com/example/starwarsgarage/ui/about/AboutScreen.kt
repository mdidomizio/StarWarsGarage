package com.example.starwarsgarage.ui.about

import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.starwarsgarage.R
import com.example.starwarsgarage.ui.SharedViewModel
import com.example.starwarsgarage.ui.TopAppBarState
import com.example.starwarsgarage.ui.theme.starJediFontFamily

data class LinkData(
    @StringRes val textRes: Int,
    @StringRes val urlRes: Int,
    val iconRes: Int
)

@Composable
fun AboutScreen(
    sharedViewModel: SharedViewModel,
    modifier: Modifier = Modifier
) {
    val screenTitle = stringResource(id = R.string.about_screen_title)
    val context = LocalContext.current

    val openUrl = { url: String ->
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        context.startActivity(intent)
    }
    val shareUrl = { url: String ->
        val intent = Intent(Intent.ACTION_SEND)
            .apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    url
                )
            }
        context.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.about_share_chooser_title)
            )
        )
    }

    LaunchedEffect(Unit) {
        sharedViewModel.updateTopAppBar(
            TopAppBarState(
                title = screenTitle
            )
        )
    }

    val followUsLinks = listOf(
        LinkData(R.string.about_github_field, R.string.about_github_profile_url, R.drawable.github),
        LinkData(R.string.about_linkedin_field, R.string.about_linkedin_url, R.drawable.linkedin),
        LinkData(R.string.about_x_field, R.string.about_x_url, R.drawable.x)
    )

    val apiLinks = listOf(
        LinkData(R.string.about_api_one_field, R.string.about_api_one_url, R.drawable.api_64dp),
        LinkData(R.string.about_api_two_field, R.string.about_api_two_url, R.drawable.api_64dp)
    )

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.padding(16.dp)
        ) {
            item {
                AboutSection(
                    title = stringResource(id = R.string.about_screen_title)
                ) {
                    ClickableLink(
                        textRes = R.string.about_share_field,
                        urlRes = R.string.about_github_repository_url,
                        onUrlClick = shareUrl,
                        icon = R.drawable.share_64dp
                    )
                }
            }

            item {
                AboutSection(
                    title = stringResource(id = R.string.about_follow_us_label)
                ) {
                    LinkSection(
                        links = followUsLinks,
                        onUrlClick = openUrl
                    )
                }
            }

            item {
                AboutSection(title = stringResource(id = R.string.about_developed_by_label)) {
                    Row(
                        modifier = Modifier
                            /*.fillMaxWidth()
                            .height(40.dp)*/
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.signature_64dp),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = stringResource(id = R.string.about_developer_name_field),
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Default,
                            modifier = modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }

            item {
                AboutSection(title = stringResource(id = R.string.about_api_label)) {
                    LinkSection(
                        links = apiLinks,
                        onUrlClick = openUrl
                    )
                }
            }

            item {
                Card {
                    Text(
                        text = stringResource(id = R.string.about_version_field),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Default,
                        modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

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
