package com.example.starwarsgarage.ui

import android.os.Message
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.domain.repository.FavoritesRepository
import com.example.starwarsgarage.domain.repository.StarshipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState
    data class Success(val favoriteStarship: List<Starship>) : FavoritesUiState
    data class Error(val message: String?) : FavoritesUiState
}

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val starshipRepository: StarshipRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<FavoritesUiState> = favoritesRepository.getFavoritesStarshipIds()
        .flatMapLatest { favoriteIds ->
            if (favoriteIds.isEmpty()) {
                flowOf(FavoritesUiState.Success(emptyList<Starship>()))
            } else {
                starshipRepository.getStarshipsByIds(favoriteIds)
                    .map<List<Starship>, FavoritesUiState> { starships ->
                        FavoritesUiState.Success(starships.sortedBy { it.name })
                    }
                    .catch { throwable ->
                        emit(FavoritesUiState.Error(throwable.message))

                    }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = FavoritesUiState.Loading
        )

    fun toggleFavorite(starship: Starship) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(starship.id)
        }
    }
}
