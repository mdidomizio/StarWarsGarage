package com.example.starwarsgarage.navigation

sealed class Screen ( val route: String) {
    object Home : Screen("home_screen")
    object Catalog: Screen("catalog_screen")
    object Favorites: Screen("favorites_screen")
    object Pdp: Screen("pdp_route/{starshipId}") {
        fun createRoute(starshipId: String) = "pdp_route/$starshipId"
    }
    object About: Screen("about_screen")
}