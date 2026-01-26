package com.example.starwarsgarage.ui

import android.icu.text.StringSearch
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

data class TopAppBarState(
    val title: String = "",
    val isVisible: Boolean = true,
    val navigationIcon: @Composable (() -> Unit)? = null,
    val actions: @Composable (RowScope.() -> Unit)? = null
)
