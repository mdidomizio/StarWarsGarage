package com.example.starwarsgarage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarsgarage.data.remote.Starship
import com.example.starwarsgarage.di.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface StarshipsListUiState {
    data class Success(val starships: List<Starship>, val hasMore: Boolean, val isLoadingMore: Boolean = false) : StarshipsListUiState
    object Error : StarshipsListUiState
    object Loading : StarshipsListUiState
}

class StarshipsViewModel : ViewModel() {

    private val repository = NetworkModule.starshipRepository

    private val _uiState = MutableStateFlow<StarshipsListUiState>(StarshipsListUiState.Loading)
    val uiState: StateFlow<StarshipsListUiState> = _uiState

    private val _starship = MutableStateFlow<Starship?>(null)
    val starship: StateFlow<Starship?> = _starship

    private var currentPage = 1
    private var isFetching = false

    init {
        getStarships()
    }

    fun getStarships() {
        viewModelScope.launch {
            _uiState.value = StarshipsListUiState.Loading
            try {
                val response = repository.getStarships(1)
                _uiState.value = StarshipsListUiState.Success(response.results, response.next != null)
                currentPage = 1
            } catch (e: Exception) {
                _uiState.value = StarshipsListUiState.Error
            }
        }
    }

    fun loadMoreStarships() {
        if (isFetching) return

        viewModelScope.launch {
            isFetching = true
            val currentState = _uiState.value
            if (currentState is StarshipsListUiState.Success && currentState.hasMore) {
                _uiState.value = currentState.copy(isLoadingMore = true)
                try {
                    val response = repository.getStarships(++currentPage)
                    val newList = currentState.starships + response.results
                    _uiState.value = StarshipsListUiState.Success(newList, response.next != null)
                } catch (e: Exception) {
                    _uiState.value = currentState.copy(isLoadingMore = false) // Keep old state on error
                }
            }
            isFetching = false
        }
    }

    fun getStarship(id: String) {
        viewModelScope.launch {
            try {
                _starship.value = repository.getStarshipById(id)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
