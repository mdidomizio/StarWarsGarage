package com.example.starwarsgarage.ui

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

sealed interface StarshipDetailUiState {
    data class Success(val starship: Starship) : StarshipDetailUiState
    object Error : StarshipDetailUiState
    object Loading : StarshipDetailUiState
}

class StarshipsViewModel : ViewModel() {

    private val repository = NetworkModule.starshipRepository

    val starships: Flow<PagingData<Starship>> = repository.getStarshipsStream()
        .cachedIn(viewModelScope)

    private val _starshipUiState = MutableStateFlow<StarshipDetailUiState>(StarshipDetailUiState.Loading)
    val starshipUiState: StateFlow<StarshipDetailUiState> = _starshipUiState

    fun getStarship(id: String) {
        viewModelScope.launch {
            _starshipUiState.value = StarshipDetailUiState.Loading
            try {
                _starshipUiState.value = StarshipDetailUiState.Success(repository.getStarshipById(id))
            } catch (e: Exception) {
                _starshipUiState.value = StarshipDetailUiState.Error
            }
        }
    }
}
