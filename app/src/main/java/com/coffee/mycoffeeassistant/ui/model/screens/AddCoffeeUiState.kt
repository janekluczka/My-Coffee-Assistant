package com.coffee.mycoffeeassistant.ui.model.screens

import android.net.Uri
import com.coffee.mycoffeeassistant.enums.Process
import com.coffee.mycoffeeassistant.enums.Roast
import com.coffee.mycoffeeassistant.enums.toDropdownMenuUiState
import com.coffee.mycoffeeassistant.ui.model.components.DropdownMenuItemUiState

data class AddCoffeeUiState(
    val processMenuUiStateList: List<DropdownMenuItemUiState<Int>> =
        Process.values().toList().map { it.toDropdownMenuUiState() },
    val roastMenuUiStateList: List<DropdownMenuItemUiState<Int>> =
        Roast.values().toList().map { it.toDropdownMenuUiState() },
    val openCalendarDialog: Boolean = false,
    val openDiscardDialog: Boolean = false,
    val isNameWrong: Boolean = false,
    val isBrandWrong: Boolean = false,
    val isAmountWrong: Boolean = false,
    val imageUri: Uri? = null,
)