package com.luczka.mycoffee.ui.models

data class SwipeableListItemUiState<T>(
    val isRevealed: Boolean = false,
    val item: T
)