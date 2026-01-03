package com.example.starwarsgarage.data.repository

import android.content.SharedPreferences
import com.example.starwarsgarage.domain.repository.FavoritesRepository
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val prefs: SharedPreferences
): FavoritesRepository {
    private val favoritesKey = "favorite_starship_ids"
    private val _favoriteStarshipIds = MutableStateFlow<Set<String>>(loadFavoriteIds())
    override val favoritesStarshipIds: StateFlow<Set<String>> = _favoriteStarshipIds.asStateFlow()

    override fun toggleFavorite(starshipId: String) {
        _favoriteStarshipIds.update { currentIds ->
            val newIds = if (currentIds.contains(starshipId)) {
                currentIds - starshipId
            } else {
                currentIds + starshipId
            }
            saveFavoriteIds(newIds)
            newIds
        }
    }
    private fun saveFavoriteIds(ids: Set<String>) {
        prefs.edit {
            putStringSet(favoritesKey, ids)
        }
    }
    private fun loadFavoriteIds(): Set<String> {
        return prefs.getStringSet(favoritesKey, emptySet()) ?: emptySet()
    }
}
