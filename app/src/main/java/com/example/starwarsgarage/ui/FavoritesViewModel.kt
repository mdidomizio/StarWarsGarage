package com.example.starwarsgarage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.domain.repository.FavoritesRepository
import com.example.starwarsgarage.domain.repository.StarshipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoritesRepository
) : ViewModel() {

    data class FavoritesUiState(
        val favoriteStarships: List<Starship> = emptyList()
    )

    val uiState: StateFlow<FavoritesUiState> = repository.favoriteStarships
        .map { favoriteSet ->
            FavoritesUiState(favoriteStarships = favoriteSet.toList())
        }
        .stateIn(
            scope =  viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = FavoritesUiState(repository.favoriteStarships.value.toList())
        )

    fun toggleFavorite(starship: Starship) {
        repository.toggleFavorite(starship)
    }
}
