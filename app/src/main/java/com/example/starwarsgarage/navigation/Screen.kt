package com.example.starwarsgarage.navigation

import com.example.starwarsgarage.navigation.AppDestinations.PDP_SCREEN_ROUTE
import com.example.starwarsgarage.navigation.AppDestinations.STARSHIP_ID_KEY

sealed class Screen ( val route: String) {
    object Home : Screen("home_screen")
    object Catalog: Screen("catalog_screen")
    object Favorites: Screen("favorites_screen")
    object Pdp: Screen("$PDP_SCREEN_ROUTE/{$STARSHIP_ID_KEY}") {
        fun createRoute(starshipId: String) = "$PDP_SCREEN_ROUTE/$starshipId"
    }
    object About: Screen("about_screen")
}
