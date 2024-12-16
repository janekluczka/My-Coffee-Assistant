package com.luczka.mycoffee.data.mappers

import com.luczka.mycoffee.data.database.types.RoastType
import com.luczka.mycoffee.domain.models.RoastModel

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