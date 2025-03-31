package com.luczka.mycoffee.ui.screens.coffeeinput

import android.net.Uri
import com.luczka.mycoffee.ui.models.ProcessUiState
import com.luczka.mycoffee.ui.models.RoastUiState

sealed class CoffeeInputAction {
    data object OnBackClicked : CoffeeInputAction()
    data object NavigateUp : CoffeeInputAction()

    data object ShowBottomSheet : CoffeeInputAction()
    data object HideBottomSheet : CoffeeInputAction()

    data object OnHideDiscardDialog : CoffeeInputAction()
    data object OnShowScaInfoDialog : CoffeeInputAction()
    data object OnHideScaInfoDialog : CoffeeInputAction()

    data object OnAddImageClicked : CoffeeInputAction()

    data class OnImagesSelected(val uris: List<Uri>) : CoffeeInputAction()
    data class OnImageClicked(val index: Int) : CoffeeInputAction()

    data class OnRoasterOrBrandValueChanged(val brand: String) : CoffeeInputAction()
    data class OnOriginOrNameValueChanged(val name: String) : CoffeeInputAction()
    data class OnRoastClicked(val roast: RoastUiState) : CoffeeInputAction()
    data class OnProcessClicked(val process: ProcessUiState) : CoffeeInputAction()
    data class OnPlantationValueChanged(val plantation: String) : CoffeeInputAction()
    data class OnScaScoreValueChanged(val scaScore: String) : CoffeeInputAction()
    data class OnAltitudeValueChanged(val altitude: String) : CoffeeInputAction()
    data class OnAdditionalInformationValueChanged(val additionalInformation: String) : CoffeeInputAction()
    data object OnSaveClicked : CoffeeInputAction()
}