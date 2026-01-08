package com.example.starwarsgarage.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.domain.repository.FavoritesRepository
import com.example.starwarsgarage.domain.repository.StarshipRepository
import com.example.starwarsgarage.navigation.AppDestinations.STARSHIP_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface StarshipPdpUiState {
    data object Loading : StarshipPdpUiState
    data class Success(
        val starship: Starship,
        val isFavorite: Boolean = false
    ) : StarshipPdpUiState

    data class Error(val message: String) : StarshipPdpUiState
}

@HiltViewModel
class StarshipPdpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: StarshipRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {
    private val starshipId: String = savedStateHandle.get<String>(STARSHIP_ID_KEY)!!
    private val _uiState = MutableStateFlow<StarshipPdpUiState>(StarshipPdpUiState.Loading)
    val uiState: StateFlow<StarshipPdpUiState> = _uiState.asStateFlow()

    fun onFavoriteToggled() {
        val currentState = _uiState.value
        if (currentState is StarshipPdpUiState.Success) {
            viewModelScope.launch {
                favoritesRepository.toggleFavorite(currentState.starship.id)
            }
        }
    }

    init {
        fetchStarshipDetails()
    }

    fun fetchStarshipDetails() {
        viewModelScope.launch {
            _uiState.value = StarshipPdpUiState.Loading

            try {
                val starship = repository.getStarshipDetailsById(starshipId).getOrThrow()
                favoritesRepository.getFavoritesStarshipIds()
                    .catch { error ->
                        val errorMessage = error.message ?: "failed to load favorites status"
                        _uiState.value = StarshipPdpUiState.Error(errorMessage)
                    }
                    .collect { favoriteIds ->
                        val isFavorite = starshipId in favoriteIds
                        _uiState.value = StarshipPdpUiState.Success(
                            starship = starship,
                            isFavorite = isFavorite
                        )
                    }

            } catch (error: Exception) {
                val errorMessage = error.message ?: "An unknown error occurred"
                _uiState.value = StarshipPdpUiState.Error(errorMessage)
            }
        }
    }
}
