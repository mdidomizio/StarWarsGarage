package com.example.starwarsgarage.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "favorites"
)

@Singleton
class FavoritesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val FAVORITE_STARSHIPS_KEY = stringSetPreferencesKey("favorite_starships")

    val favoriteStarshipIds: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[FAVORITE_STARSHIPS_KEY] ?: emptySet()
        }

    suspend fun toggleFavorite(starshipId: String) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITE_STARSHIPS_KEY] ?: emptySet()
            val newFavorites = if (currentFavorites.contains(starshipId)) {
                currentFavorites - starshipId
            } else {
                currentFavorites + starshipId
            }
            Timber.tag("Miriam").d("New favorites: %s", newFavorites)
            preferences[FAVORITE_STARSHIPS_KEY] = newFavorites
        }
    }
}
