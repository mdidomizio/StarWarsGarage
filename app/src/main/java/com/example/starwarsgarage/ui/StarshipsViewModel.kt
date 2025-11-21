package com.example.starwarsgarage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarsgarage.data.remote.Starship
import com.example.starwarsgarage.di.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StarshipsViewModel : ViewModel() {

    private val repository = NetworkModule.starshipRepository

    private val _starships = MutableStateFlow<List<Starship>>(emptyList())
    val starships: StateFlow<List<Starship>> = _starships

    private val _starship = MutableStateFlow<Starship?>(null)
    val starship: StateFlow<Starship?> = _starship

    init {
        getStarships()
    }

    private fun getStarships() {
        viewModelScope.launch {
            try {
                _starships.value = repository.getStarships()
            } catch (e: Exception) {
                // Handle error
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