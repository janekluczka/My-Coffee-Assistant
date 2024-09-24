package com.luczka.mycoffee.ui.screens.coffeeinput

import android.net.Uri
import com.luczka.mycoffee.domain.models.Process
import com.luczka.mycoffee.domain.models.Roast

sealed class CoffeeInputAction {
    object NavigateUp : CoffeeInputAction()
    data class OnImageUriChanged(val uri: Uri?) : CoffeeInputAction()
    object OnDeleteImageClicked : CoffeeInputAction()
    data class OnNameValueChanged(val name: String) : CoffeeInputAction()
    object OnNameInputFinished : CoffeeInputAction()
    data class OnBrandValueChanged(val brand: String) : CoffeeInputAction()
    object OnBrandInputFinished : CoffeeInputAction()
    data class OnAmountValueChanged(val amount: String) : CoffeeInputAction()
    data class OnScaScoreValueChanged(val scaScore: String) : CoffeeInputAction()
    data class OnProcessChanged(val process: Process?) : CoffeeInputAction()
    data class OnRoastChanged(val roast: Roast?) : CoffeeInputAction()
    object OnSaveClicked : CoffeeInputAction()
}