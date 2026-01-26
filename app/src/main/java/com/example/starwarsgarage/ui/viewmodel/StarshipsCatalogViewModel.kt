package com.example.starwarsgarage.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.domain.repository.FavoritesRepository
import com.example.starwarsgarage.domain.repository.StarshipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
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
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery
    val starships = _searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            starshipRepository.getStarshipsStream(query)
        }
        .cachedIn(viewModelScope)
    /*val starships: Flow<PagingData<Starship>> = starshipRepository.getStarshipsStream(null)
        .cachedIn(viewModelScope)*/
    val uiState: StateFlow<CatalogUiState> =
        favoritesRepository.getFavoritesStarshipIds()
            .catch { exception ->
                emit(emptySet())
            }
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

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }
}