package com.example.starwarsgarage.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoritesStarshipIds(): Flow<Set<String>>
    suspend fun toggleFavorite(starshipId: String)
}
