package com.example.starwarsgarage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.starwarsgarage.data.local.FavoritesDataStore
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.domain.repository.StarshipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: StarshipRepository,
    private val favoritesDataStore: FavoritesDataStore
) : ViewModel() {

    val favoriteStarships: Flow<PagingData<Starship>> =
        combine(
            repository.getFavoriteStarshipsStream(favoritesDataStore.favoriteStarshipIds),
            favoritesDataStore.favoriteStarshipIds
        ) { pagingData, favorites ->
            pagingData.map { starship ->
                starship.copy(isFavorite = favorites.contains(starship.id))
            }
        }.cachedIn(viewModelScope)

    fun onToggleFavorite(starshipId: String) {
        viewModelScope.launch {
            Timber.tag("Miriam").d("Toggling favorite for starship ID from favorites view model: %s", starshipId)
            favoritesDataStore.toggleFavorite(starshipId)
        }
    }
}
