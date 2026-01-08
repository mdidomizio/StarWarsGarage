package com.example.starwarsgarage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.starwarsgarage.navigation.AppNavigation
import com.example.starwarsgarage.ui.AppBottomNavigation
import com.example.starwarsgarage.ui.SharedViewModel
import com.example.starwarsgarage.ui.theme.StarWarsGarageTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarWarsGarageTheme {
                val navController = rememberNavController()
                val sharedViewModel: SharedViewModel = hiltViewModel()
                val topAppBarState by sharedViewModel.topAppBarState.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(topAppBarState.title) },
                            navigationIcon = { topAppBarState.navigationIcon?.invoke() },
                            actions = { topAppBarState.actions?.invoke(this) }
                        )
                    },
                    bottomBar = { AppBottomNavigation(navController = navController) }
                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        sharedViewModel = sharedViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
