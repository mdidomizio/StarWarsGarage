package com.example.starwarsgarage.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.starwarsgarage.ui.TopAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private val _topAppBarState = MutableStateFlow(TopAppBarState())
    val topAppBarState = _topAppBarState.asStateFlow()

    fun updateTopAppBar(newState: TopAppBarState) {
        _topAppBarState.value = newState
    }
}