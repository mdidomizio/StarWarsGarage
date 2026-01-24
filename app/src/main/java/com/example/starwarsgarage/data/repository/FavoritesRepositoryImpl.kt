package com.example.starwarsgarage.data.repository

import android.util.Log
import com.example.starwarsgarage.domain.repository.FavoritesRepository
import com.example.starwarsgarage.data.local.FavoriteStarship
import com.example.starwarsgarage.data.local.FavoriteStarshipDao
import com.example.starwarsgarage.data.local.StarWarsDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val favoriteStarshipDao: FavoriteStarshipDao,
    private val database: StarWarsDatabase
): FavoritesRepository {
    init {
        Log.d("HiltDebug", "Repository initialized")
        Log.d("HiltDebug", "DAO instance: ${favoriteStarshipDao.hashCode()}")
        Log.d("HiltDebug", "Database instance: ${database.hashCode()}")
    }
    override fun getFavoritesStarshipIds(): Flow<Set<String>> {
        return favoriteStarshipDao.getFavoriteIds().map { it.toSet() }
    }

    override suspend fun toggleFavorite(starshipId: String) {
        val favorite = favoriteStarshipDao.getFavoriteById(starshipId)
        if (favorite != null) {
            favoriteStarshipDao.delete(favorite)
        } else {
            favoriteStarshipDao.insert(FavoriteStarship(id = starshipId))
        }
    }
}
