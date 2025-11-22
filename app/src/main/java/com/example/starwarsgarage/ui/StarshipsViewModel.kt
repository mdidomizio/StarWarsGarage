package com.example.starwarsgarage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarsgarage.data.remote.Starship
import com.example.starwarsgarage.di.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface StarshipsListUiState {
    data class Success(val starships: List<Starship>) : StarshipsListUiState
    object Error : StarshipsListUiState
    object Loading : StarshipsListUiState
}

class StarshipsViewModel : ViewModel() {

    private val repository = NetworkModule.starshipRepository

    private val _uiState = MutableStateFlow<StarshipsListUiState>(StarshipsListUiState.Loading)
    val uiState: StateFlow<StarshipsListUiState> = _uiState

    private val _starship = MutableStateFlow<Starship?>(null)
    val starship: StateFlow<Starship?> = _starship

    init {
        getStarships()
    }

    fun getStarships() {
        viewModelScope.launch {
            _uiState.value = StarshipsListUiState.Loading
            try {
                _uiState.value = StarshipsListUiState.Success(repository.getStarships())
            } catch (e: Exception) {
                _uiState.value = StarshipsListUiState.Error
            }
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
