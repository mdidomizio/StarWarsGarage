package com.example.starwarsgarage.domain.repository

import android.content.SharedPreferences
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.ui.catalog.StarshipCard
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(
    private val prefs: SharedPreferences,
    private val moshi: Moshi
){
    private val favoritesPrefsKey = "favorite_starship_json"
    private val _favoriteStarships = MutableStateFlow<Set<Starship>>(loadFavorites())
    val favoriteStarships: StateFlow<Set<Starship>> = _favoriteStarships
    val favoriteStarshipIds: StateFlow<Set<String>> =
        MutableStateFlow(loadFavorites().map { it.id}.toSet())
            .also { idsFlow ->
                _favoriteStarships.update {
                    idsFlow.value = it.map { starship -> starship.id }.toSet()
                    it
                }
            }

    fun toggleFavorite(starship: Starship) {
        _favoriteStarships.update { currentFavorites ->
            val isCurrentlyFavorite = currentFavorites.any{ it.id == starship.id }
            val newFavorites = if (isCurrentlyFavorite) {
                currentFavorites.filterNot { it.id == starship.id }.toSet()
            } else {
                currentFavorites + starship
            }
            saveFavorites(newFavorites)
            newFavorites
        }
    }

    private fun saveFavorites(favorites: Set<Starship>) {
        val jsonAdapter = moshi.adapter<Set<Starship>>(
            Types.newParameterizedType(Set::class.java, Starship::class.java)
        )
        val jsonString = jsonAdapter.toJson(favorites)
        prefs.edit().putString(favoritesPrefsKey, jsonString).apply()
        (favoriteStarshipIds as MutableStateFlow).value = favorites.map { it.id }.toSet()
    }

    private fun loadFavorites(): Set<Starship> {
        val jsonString = prefs.getString(favoritesPrefsKey, null) ?: return emptySet()
        val jsonAdapter =  moshi.adapter<Set<Starship>>(
            Types.newParameterizedType(Set::class.java, Starship::class.java)
        )
        return try {
            jsonAdapter.fromJson(jsonString) ?: emptySet()
        } catch (e: Exception) {
            emptySet()
        }
    }
}



