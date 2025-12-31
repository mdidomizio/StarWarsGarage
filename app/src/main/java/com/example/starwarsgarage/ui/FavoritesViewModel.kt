package com.example.starwarsgarage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.domain.repository.StarshipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: StarshipRepository
) : ViewModel() {

    val favoriteStarships = repository.getFavoriteStarships()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleFavorite(starshipId: String) {
        Timber.tag("miriam").d("toggleFavorite called for $starshipId")
        viewModelScope.launch {
            val isCurrentlyFavorite = favoriteStarships.value.any { it.id == starshipId }

            if (isCurrentlyFavorite) {
                repository.removeFavoriteStarship(starshipId)
                Timber.tag("miriam").d("$starshipId removed from favorites")
            } else {
                repository.getStarshipDetailsById(starshipId)
                    .onSuccess { newFavoriteStarship ->
                        repository.addFavoriteStarship(newFavoriteStarship)
                        Timber.tag("miriam").d("${newFavoriteStarship.name} added to favorites")
                    }
                    .onFailure { error ->
                        Timber.tag("miriam").e(
                            error,
                            "Failed to add favorite: Could not fetch details for $starshipId"
                        )
                    }
            }
        }
    }
}
