package com.coffee.mycoffeeassistant.ui.model.components

data class DropdownMenuItemUiState<T>(
    val id: T,
    val description: String?,
    val stringResource: Int?,
)
