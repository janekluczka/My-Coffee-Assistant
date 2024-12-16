package com.luczka.mycoffee.ui.mappers

import com.luczka.mycoffee.domain.models.RoastModel
import com.luczka.mycoffee.ui.models.RoastUiState

fun RoastModel.toUiState(): RoastUiState {
    return when (this) {
        RoastModel.Light -> RoastUiState.Light
        RoastModel.Medium -> RoastUiState.Medium
        RoastModel.MediumDark -> RoastUiState.MediumDark
        RoastModel.Dark -> RoastUiState.Dark
    }
}

fun RoastUiState.toModel(): RoastModel {
    return when (this) {
        RoastUiState.Light -> RoastModel.Light
        RoastUiState.Medium -> RoastModel.Medium
        RoastUiState.MediumDark -> RoastModel.MediumDark
        RoastUiState.Dark -> RoastModel.Dark
    }
}