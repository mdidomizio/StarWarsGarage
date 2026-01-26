package com.example.starwarsgarage.data.repository

import com.example.starwarsgarage.domain.repository.FavoritesRepository
import com.example.starwarsgarage.data.local.FavoriteStarshipEntity
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

    override fun getFavoritesStarshipIds(): Flow<Set<String>> {
        return favoriteStarshipDao.getFavoriteIds().map { it.toSet() }
    }

    override suspend fun toggleFavorite(starshipId: String) {
        val favorite = favoriteStarshipDao.getFavoriteById(starshipId)
        if (favorite != null) {
            favoriteStarshipDao.delete(favorite)
        } else {
            favoriteStarshipDao.insert(FavoriteStarshipEntity(id = starshipId))
        }
    }
}
