package com.example.starwarsgarage.ui.about

import android.R.attr.type
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.starwarsgarage.R
import com.example.starwarsgarage.ui.SharedViewModel
import com.example.starwarsgarage.ui.TopAppBarState
import com.example.starwarsgarage.ui.theme.starJediFontFamily

@Composable
fun AboutScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    modifier: Modifier = Modifier
) {
    val screenTitle = stringResource(id = R.string.about_screen_title)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        sharedViewModel.updateTopAppBar(
            TopAppBarState(
                title = screenTitle
            )
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
        ) {
            item {
                AboutSection(
                    title = stringResource(id = R.string.about_screen_title),
                ) {
                    val githubRepositoryUrl =
                        stringResource(id = R.string.about_github_repository_url)
                    Text(
                        text = stringResource(id = R.string.about_share_field),
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = modifier
                            .padding(16.dp)
                            .clickable {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        githubRepositoryUrl
                                    )
                                }
                                context.startActivity(
                                    Intent.createChooser(
                                        shareIntent,
                                        context.getString(R.string.about_share_chooser_title)
                                    )
                                )

                            }
                    )
                }
            }

            item {
                AboutSection(
                    title = stringResource(id = R.string.about_follow_us_label),
                ) {
                    val githubProfileUrl = stringResource(id = R.string.about_github_profile_url)
                    Text(
                        text = stringResource(id = R.string.about_github_field),
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = modifier
                            .padding(16.dp)
                            .clickable {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(githubProfileUrl)
                                )
                                context.startActivity(intent)
                            }
                    )

                    val linkedInUrl = stringResource(id = R.string.about_linkedin_url)
                    Text(
                        text = stringResource(id = R.string.about_linkedin_field),
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = modifier
                            .padding(start = 16.dp, top = 0.dp, bottom = 16.dp)
                            .clickable {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(linkedInUrl)
                                )
                                context.startActivity(intent)
                            }
                    )

                    val xUrl = stringResource(id = R.string.about_x_url)
                    Text(
                        text = stringResource(id = R.string.about_x_field),
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = modifier
                            .padding(start = 16.dp, top = 0.dp, bottom = 16.dp)
                            .clickable {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(xUrl)
                                )
                                context.startActivity(intent)
                            }
                    )
                }
            }
            item {
                AboutSection(
                    title = stringResource(id = R.string.about_developed_by_label),
                ) {
                    Text(
                        text = stringResource(id = R.string.about_developer_name_field),
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default,
                        /*color = MaterialTheme.colorScheme.primary,*/
                        modifier = modifier.padding(16.dp)
                    )
                }
            }
            item {
                AboutSection(
                    title = stringResource(id = R.string.about_api_label),
                ) {
                    val apiOneUrl = stringResource(id = R.string.about_api_one_url)
                    Text(
                        text = stringResource(id = R.string.about_api_one_field),
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = modifier
                            .padding(16.dp)
                            .clickable {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(apiOneUrl)
                                )
                                context.startActivity(intent)
                            }
                    )

                    val apiTwoUrl = stringResource(id = R.string.about_api_two_url)
                    Text(
                        text = stringResource(id = R.string.about_api_two_field),
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = modifier
                            .padding(start = 16.dp, top = 0.dp, bottom = 16.dp)
                            .clickable {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(apiTwoUrl)
                                )
                                context.startActivity(intent)
                            }
                    )
                }
            }
            item {
                Card {
                    Text(
                        text = stringResource(id = R.string.about_version_field),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Default,
                        modifier = modifier.padding(8.dp)
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
            modifier = Modifier
                .fillMaxWidth()
        ) {
            content()
        }
    }
}
