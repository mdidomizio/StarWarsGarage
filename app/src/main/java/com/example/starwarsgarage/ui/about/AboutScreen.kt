package com.example.starwarsgarage.ui.about

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.starwarsgarage.R
import com.example.starwarsgarage.ui.SharedViewModel
import com.example.starwarsgarage.ui.TopAppBarState

@Composable
fun AboutScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    modifier: Modifier = Modifier
) {
    val screenTitle = stringResource(id = R.string.about_screen_title)

    LaunchedEffect(Unit) {
        sharedViewModel.updateTopAppBar(
            TopAppBarState(
                title = screenTitle,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_content_description)
                        )
                    }
                }
            )
        )
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "this is the about screen"
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "this is the about screen"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "this is the about screen"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "this is the about screen"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }


}
