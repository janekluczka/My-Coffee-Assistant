package com.luczka.mycoffee.ui.screens.coffeeinput

import android.net.Uri
import com.luczka.mycoffee.ui.models.ProcessUiState
import com.luczka.mycoffee.ui.models.RoastUiState

sealed class CoffeeInputAction {
    data object NavigateUp : CoffeeInputAction()
    data class OnImageUriChanged(val uri: Uri?) : CoffeeInputAction()
    data object OnDeleteImageClicked : CoffeeInputAction()
    data class OnNameValueChanged(val name: String) : CoffeeInputAction()
    data object OnNameInputFinished : CoffeeInputAction()
    data class OnBrandValueChanged(val brand: String) : CoffeeInputAction()
    data object OnBrandInputFinished : CoffeeInputAction()
    data class OnAmountValueChanged(val amount: String) : CoffeeInputAction()
    data class OnScaScoreValueChanged(val scaScore: String) : CoffeeInputAction()
    data class OnProcessClicked(val process: ProcessUiState) : CoffeeInputAction()
    data class OnRoastClicked(val roast: RoastUiState) : CoffeeInputAction()
    data object OnSaveClicked : CoffeeInputAction()
}