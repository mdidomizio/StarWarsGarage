package com.example.starwarsgarage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor() : ViewModel() {
    private val _favouriteStarships = MutableStateFlow<Set<String>>(emptySet())
    val favouriteStarships: StateFlow<Set<String>> = _favouriteStarships

    fun toggleFavourite(starshipId: String) {
        viewModelScope.launch {
            _favouriteStarships.update { currentFavourites ->
                val newFavourites = currentFavourites.toMutableSet()
                if (newFavourites.contains(starshipId)) {
                    newFavourites.remove(starshipId)
                } else {
                    newFavourites.add(starshipId)
                }
                newFavourites
            }
        }
    }
    fun isFavourite(starshipId: String): Boolean {
        return _favouriteStarships.value.contains(starshipId)
    }
}
