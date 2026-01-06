package com.example.starwarsgarage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.domain.repository.FavoritesRepository
import com.example.starwarsgarage.domain.repository.StarshipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarshipsCatalogViewModel @Inject constructor(
    starshipRepository: StarshipRepository,
    private val favoritesRepository: FavoritesRepository
): ViewModel() {

    data class CatalogUiState(
        val favoriteIds: Set<String> = emptySet()
    )
    val starships: Flow<PagingData<Starship>> = starshipRepository.getStarshipsStream()
        .cachedIn(viewModelScope)
    val uiState: StateFlow<CatalogUiState> =
        favoritesRepository.getFavoritesStarshipIds()
            .map { ids -> CatalogUiState(favoriteIds = ids) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = CatalogUiState()
            )

    fun onFavoriteToggled(starship: Starship) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(starship.id)
        }
    }
}
