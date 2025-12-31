package com.example.starwarsgarage.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.domain.repository.StarshipRepository
import com.example.starwarsgarage.navigation.AppDestinations.STARSHIP_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface StarshipPdpUiState {
    data object Loading : StarshipPdpUiState
    data class Success(val starship: Starship) : StarshipPdpUiState
    data class Error(val message: String) : StarshipPdpUiState
}

@HiltViewModel
class StarshipPdpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: StarshipRepository
) : ViewModel() {
    private val starshipId: String = savedStateHandle.get<String>(STARSHIP_ID_KEY)!!
    private val _uiState = MutableStateFlow<StarshipPdpUiState>(StarshipPdpUiState.Loading)
    val uiState: StateFlow<StarshipPdpUiState> = _uiState.asStateFlow()

    init {
        fetchStarshipDetails()
    }

    fun fetchStarshipDetails() {
        viewModelScope.launch {
            _uiState.value = StarshipPdpUiState.Loading

            repository.getStarshipDetailsById(starshipId)
                .onSuccess { starship ->
                    _uiState.value = StarshipPdpUiState.Success(starship)
                }
                .onFailure { error ->
                    val errorMessage = error.message ?: "An unknown error occurred"
                    _uiState.value = StarshipPdpUiState.Error(errorMessage)
                }
        }
    }
}
