package com.example.starwarsgarage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.starwarsgarage.ui.theme.StarWarsGarageTheme
import com.example.starwarsgarage.ui.StarshipsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarWarsGarageTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StarshipsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun StarshipsScreen(modifier: Modifier = Modifier, viewModel: StarshipsViewModel = StarshipsViewModel()) {
    val starships by viewModel.starships.collectAsState()

    LazyColumn(modifier = modifier) {
        items(starships) { starship ->
            Text(text = starship.name)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StarshipsScreenPreview() {
    StarWarsGarageTheme {
        StarshipsScreen()
    }
}
