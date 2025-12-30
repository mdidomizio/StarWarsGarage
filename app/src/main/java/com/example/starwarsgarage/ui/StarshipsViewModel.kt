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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

sealed interface UiState {
    data class Success(val starshipProduct: Starship) : UiState
    data class Error(val message: String? = null) : UiState
    object Loading : UiState
}

@HiltViewModel
class StarshipsViewModel @Inject constructor(
    private val repository: StarshipRepository,
    private val favoritesDataStore: FavoritesDataStore
) : ViewModel() {

    private val favoriteStarshipIds: StateFlow<Set<String>> =
        favoritesDataStore.favoriteStarshipIds
        .onEach { ids -> Timber.tag("Miriam").d("Favorite IDs updated: $ids") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    val starships: Flow<PagingData<Starship>> =
        combine(
            repository.getStarshipsStream(),
            favoriteStarshipIds
        ) { pagingData, favorites ->
            pagingData.map { starship ->
                starship.copy(isFavorite = favorites.contains(starship.id))
            }
        }
        .onEach { Timber.tag("Miriam").d("Starships PagingData updated") }

    private val _starshipUiState =
        MutableStateFlow<UiState>(UiState.Loading)
    val starshipUiState: StateFlow<UiState> = _starshipUiState

    fun onToggleFavorite(starshipId: String) {
        viewModelScope.launch {
            Timber.tag("Miriam").d("Toggling favorite for starship ID from catalog: %s", starshipId)
            favoritesDataStore.toggleFavorite(starshipId)
        }
    }

    fun getStarship(id: String) {
        viewModelScope.launch {
            Timber.tag("Miriam").d("Getting starship with id: $id")
            _starshipUiState.value = UiState.Loading
            repository.getStarshipProduct(id)
                .onSuccess { product ->
                    Timber.tag("Miriam").d("Successfully got starship: ${product.name}")
                    _starshipUiState.value = UiState.Success(product)
                }
                .onFailure {
                    Timber.tag("Miriam").e(it, "Error getting starship with id: $id")
                    _starshipUiState.value = UiState.Error(it.message)
                }
        }
    }

    fun retry(id: String) {
        Timber.tag("Miriam").d("Retrying getStarship for id: $id")
        getStarship(id)
    }
}
