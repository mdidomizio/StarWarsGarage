package com.example.starwarsgarage.ui

import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.starwarsgarage.data.remote.Starship
import com.example.starwarsgarage.di.NetworkModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface UiState {
    data class Success(val starship: Starship) : UiState
    object Error : UiState
    object Loading : UiState
}

class StarshipsViewModel : ViewModel() {

    private val repository = NetworkModule.starshipRepository

    val starships: Flow<PagingData<Starship>> = repository.getStarshipsStream()
        .cachedIn(viewModelScope)

    private val _starshipUiState =
        MutableStateFlow<UiState>(UiState.Loading)
    val starshipUiState: StateFlow<UiState> = _starshipUiState

    fun getStarship(id: String) {
        viewModelScope.launch {
            _starshipUiState.value = UiState.Loading
            try {
                _starshipUiState.value =
                    UiState.Success(repository.getStarshipById(id))
            } catch (e: Exception) {
                _starshipUiState.value = UiState.Error
            }
        }
    }
}
