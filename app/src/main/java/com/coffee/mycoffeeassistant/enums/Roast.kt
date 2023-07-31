package com.coffee.mycoffeeassistant.enums

import com.coffee.mycoffeeassistant.R
import com.coffee.mycoffeeassistant.ui.model.components.DropdownMenuItemUiState

enum class Roast(val id: Int, val stringResource: Int) {
    Light(1, R.string.roast_light),
    Medium(2, R.string.roast_medium),
    MediumDark(3, R.string.roast_medium_dark),
    Dark(4, R.string.roast_dark)
}

fun Roast.toDropdownMenuUiState(): DropdownMenuItemUiState<Int> =
    DropdownMenuItemUiState(
        id = id,
        description = null,
        stringResource = stringResource
    )

fun getRoastStringResource(id: Int): Int? {
    val roast = Roast.values().firstOrNull { it.id == id }
    return roast?.stringResource
}