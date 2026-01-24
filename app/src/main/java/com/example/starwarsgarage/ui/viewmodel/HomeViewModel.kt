package com.example.starwarsgarage.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarsgarage.data.local.ShowstopperDataStoreManager
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.domain.repository.StarshipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ShowstopperState<T>(
    val item: T? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val starshipRepository: StarshipRepository,
    private val showstopperManager: ShowstopperDataStoreManager
) : ViewModel() {
    private val _starshipState = MutableStateFlow(ShowstopperState<Starship>())
    val starshipState: StateFlow<ShowstopperState<Starship>> = _starshipState

    init {
        loadStarshipOfTheDay()
    }

    fun loadStarshipOfTheDay() {
        viewModelScope.launch {
            _starshipState.value = _starshipState.value.copy(isLoading = true, errorMessage = null)
            if (showstopperManager.isNewStarshipNeeded()) {
                fetchAndSaveNewStarship()
            } else {
                loadSavedStarship()
            }
            _starshipState.value = _starshipState.value.copy(isLoading = false)
        }
    }

    private suspend fun fetchAndSaveNewStarship() {
        try {
            val result = starshipRepository.getRandomStarship()

            result.onSuccess { starship ->
                if (starship != null) {
                    showstopperManager.saveDailyStarship(starship.id)
                    _starshipState.value = _starshipState.value.copy(
                        item = starship,
                        errorMessage = null
                    )
                } else {
                    _starshipState.value = _starshipState.value.copy(
                        errorMessage = "No starship available"
                    )
                }
            }.onFailure { error ->
                _starshipState.value = _starshipState.value.copy(
                    errorMessage = "Failed to load starship: ${error.message}"
                )
            }
        } catch (e: Exception) {
            _starshipState.value = _starshipState.value.copy(
                errorMessage = "Unexpected error: ${e.message}"
            )
        }
    }


    private suspend fun loadSavedStarship() {
        val savedStarshipId = showstopperManager.getDailyStarshipId()

        if (savedStarshipId == null) {
            fetchAndSaveNewStarship()
            return
        }
        try {
            val starship = starshipRepository
                .getStarshipsByIds(setOf(savedStarshipId))
                .first()
                .firstOrNull()

            if (starship != null) {
                _starshipState.value = _starshipState.value.copy(
                    item = starship,
                    errorMessage = null
                )
            } else {
                fetchAndSaveNewStarship()
            }
        } catch (e: Exception) {
            fetchAndSaveNewStarship()
        }
    }

    fun retryStarship() {
        loadStarshipOfTheDay()
    }

    fun clearStarshipError() {
        _starshipState.value = _starshipState.value.copy(errorMessage = null)
    }
}
