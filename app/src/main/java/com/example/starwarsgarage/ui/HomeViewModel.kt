package com.example.starwarsgarage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.domain.repository.StarshipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val starshipRepository: StarshipRepository
) : ViewModel() {
    private val _starshipOfTheDay = MutableStateFlow<Starship?>(null)
    val starshipOfTheDay: StateFlow<Starship?> = _starshipOfTheDay

    init {
        fetchStarshipOfTheDay()
    }

    fun fetchStarshipOfTheDay() {
        viewModelScope.launch {
            val randomStarshipResult = starshipRepository.getRandomStarship()
            _starshipOfTheDay.value = randomStarshipResult.getOrNull()
        }
    }
}
