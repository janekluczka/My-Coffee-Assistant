package com.coffee.mycoffeeassistant.ui.model

import com.coffee.mycoffeeassistant.R

data class CupboardUiState(
    val currentTab: Int = 0,
    val tabTitleResources: List<Int> = listOf(
        R.string.tab_cupboard_my_stock,
        R.string.tab_cupboard_my_favourites
    )
)