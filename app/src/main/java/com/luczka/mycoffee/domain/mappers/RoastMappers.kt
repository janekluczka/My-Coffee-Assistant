package com.luczka.mycoffee.domain.mappers

import com.luczka.mycoffee.data.database.types.RoastType
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

fun RoastType.toModel(): RoastModel {
    return when (this) {
        RoastType.Light -> RoastModel.Light
        RoastType.Medium -> RoastModel.Medium
        RoastType.MediumDark -> RoastModel.MediumDark
        RoastType.Dark -> RoastModel.Dark
    }
}

fun RoastModel.toType(): RoastType {
    return when (this) {
        RoastModel.Light -> RoastType.Light
        RoastModel.Medium -> RoastType.Medium
        RoastModel.MediumDark -> RoastType.MediumDark
        RoastModel.Dark -> RoastType.Dark
    }
}