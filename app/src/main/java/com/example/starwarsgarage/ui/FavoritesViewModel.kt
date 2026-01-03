package com.example.starwarsgarage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.domain.repository.FavoritesRepository
import com.example.starwarsgarage.domain.repository.StarshipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val starshipRepository: StarshipRepository
) : ViewModel() {

    data class FavoritesUiState(
        val favoriteStarships: List<Starship> = emptyList(),
        val isLoading: Boolean = true
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<FavoritesUiState> = favoritesRepository.favoritesStarshipIds
        .flatMapLatest { favoriteIds ->
            starshipRepository.getStarshipsByIds(favoriteIds)
        }
        .map { starships ->
            FavoritesUiState(favoriteStarships = starships, isLoading = false)
        }
        .stateIn(
            scope =  viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = FavoritesUiState(isLoading = true)
        )

    fun toggleFavorite(starship: Starship) {
        favoritesRepository.toggleFavorite(starship.id)
    }
}
