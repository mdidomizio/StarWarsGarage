package com.example.starwarsgarage.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface FavoritesRepository {
    val favoritesStarshipIds: StateFlow<Set<String>>
    fun toggleFavorite(starshipId: String)
}
