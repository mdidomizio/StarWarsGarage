package com.example.starwarsgarage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.domain.repository.StarshipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: StarshipRepository
) : ViewModel() {
    private val _favoriteStarships = MutableStateFlow<List<Starship>>(emptyList())
    val favoriteStarships = _favoriteStarships.asStateFlow()

    fun toggleFavorite(starshipId: String) {
        viewModelScope.launch {
            val currentFavorites = _favoriteStarships.value
            val isCurrentlyFavorite = currentFavorites.any { it.id == starshipId }

            if (isCurrentlyFavorite) {
                _favoriteStarships.value = currentFavorites.filterNot { it.id == starshipId }
            } else {
                repository.getStarshipDetailsById(starshipId)
                    .onSuccess { newFavoriteStarship ->
                        _favoriteStarships.value = currentFavorites + newFavoriteStarship
                    }
                    .onFailure {
                        Timber.e(
                            it,
                            "Failed to add favorite: Could not fetch details for $starshipId"
                        )
                    }
            }
        }
    }

    fun isFavourite(starshipId: String): Boolean {
        return _favoriteStarships.value.any { it.id == starshipId }
    }
}
