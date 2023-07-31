package com.coffee.mycoffeeassistant.enums

import com.coffee.mycoffeeassistant.R
import com.coffee.mycoffeeassistant.ui.model.components.DropdownMenuItemUiState

enum class Process(val id: Int, val stringResource: Int) {
    Natural(1, R.string.process_natural),
    Washed(2, R.string.process_washed),
    Honey(3, R.string.process_honey)
}

fun Process.toDropdownMenuUiState(): DropdownMenuItemUiState<Int> =
    DropdownMenuItemUiState(
        id = id,
        description = null,
        stringResource = stringResource
    )

fun getProcessStringResource(id: Int): Int? {
    val process = Process.values().firstOrNull { it.id == id }
    return process?.stringResource
}