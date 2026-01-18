package com.example.starwarsgarage.ui

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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val starshipRepository: StarshipRepository,
    private val showstopperManager: ShowstopperDataStoreManager
) : ViewModel() {
    private val _starshipOfTheDay = MutableStateFlow<Starship?>(null)
    val starshipOfTheDay: StateFlow<Starship?> = _starshipOfTheDay

    init {
        loadStarshipOfTheDay()
    }

    fun loadStarshipOfTheDay() {
        viewModelScope.launch {
            if (showstopperManager.isNewStarshipNeeded()) {
                fetchAndSaveNewStarship()
            } else {
                loadSavedStarship()
            }
        }
    }

    private suspend fun fetchAndSaveNewStarship() {
        try {
            val result = starshipRepository.getRandomStarship()

            result.onSuccess { starship ->
                if (starship != null) {
                    showstopperManager.saveDailyStarship(
                        starship.id,
                        System.currentTimeMillis()
                    )
                    _starshipOfTheDay.value = starship
                }
            }.onFailure {
                // TODO Handle failure to get the flow
            }
        } catch (e: Exception) {
            // TODO Handle other exceptions, e.g., during flow collection
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

            _starshipOfTheDay.value = starship
        } catch (e: Exception) {
            fetchAndSaveNewStarship()
        }
    }
}
