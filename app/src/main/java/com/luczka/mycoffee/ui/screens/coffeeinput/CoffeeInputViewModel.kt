package com.luczka.mycoffee.ui.screens.coffeeinput

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.ui.mappers.toModel
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.ui.models.CoffeeImageUiState
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.models.ProcessUiState
import com.luczka.mycoffee.ui.models.RoastUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CoffeeInputUiState(
    val roasts: List<RoastUiState> = RoastUiState.entries,
    val processes: List<ProcessUiState> = ProcessUiState.entries,

    val isEdit: Boolean = false,
    val newOrUpdatedCoffeeUiState: CoffeeUiState = CoffeeUiState(),
    val oldCoffeeUiState: CoffeeUiState? = null,

    val openBottomSheet: Boolean = false,
    val openDiscardDialog: Boolean = false,
    val openScaInfoDialog: Boolean = false,

    val showImage: Boolean = false,
    val selectedImageIndex: Int? = null,

    val isRoasterOrBrandError: Boolean = false,
    val isOriginOrNameError: Boolean = false,
    val isScaScoreError: Boolean = false,
    val isAdditionalInformationError: Boolean = false,
    val additionalInformationMaxLength: Int = 200,

    val deleteImage: Boolean = false,
    val isLoading: Boolean = false,
)

@AssistedFactory
interface CoffeeInputViewModelFactory {
    fun create(coffeeId: Long?): CoffeeInputViewModel
}

@HiltViewModel(assistedFactory = CoffeeInputViewModelFactory::class)
class CoffeeInputViewModel @AssistedInject constructor(
    @Assisted coffeeId: Long? = null,
    private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoffeeInputUiState())
    val uiState: StateFlow<CoffeeInputUiState> = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<CoffeeInputNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    init {
        if (coffeeId != null) {
            viewModelScope.launch {
                myCoffeeDatabaseRepository.getCoffee(coffeeId)?.let { coffeeModel ->
                    _uiState.update {
                        it.copy(
                            isEdit = true,
                            newOrUpdatedCoffeeUiState = coffeeModel.toUiState(),
                            oldCoffeeUiState = coffeeModel.toUiState()
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: CoffeeInputAction) {
        when (action) {
            is CoffeeInputAction.OnBackClicked -> back()
            is CoffeeInputAction.NavigateUp -> navigateUp()
            is CoffeeInputAction.OnAddImageClicked -> {}

            is CoffeeInputAction.ShowBottomSheet -> showBottomSheet()
            is CoffeeInputAction.HideBottomSheet -> hideBottomSheet()

            is CoffeeInputAction.OnHideDiscardDialog -> hideDiscardDialog()
            is CoffeeInputAction.OnShowScaInfoDialog -> showScaInfoDialog()
            is CoffeeInputAction.OnHideScaInfoDialog -> hideScaInfoDialog()

            is CoffeeInputAction.OnImagesSelected -> updateImagesUris(action.uris)
            is CoffeeInputAction.OnImageClicked -> showImage(action.index)

            is CoffeeInputAction.OnRoasterOrBrandValueChanged -> updateBrand(action.brand)
            is CoffeeInputAction.OnOriginOrNameValueChanged -> updateName(action.name)
            is CoffeeInputAction.OnAmountValueChanged -> updateAmount(action.amount)
            is CoffeeInputAction.OnRoastClicked -> updateRoast(action.roast)
            is CoffeeInputAction.OnProcessClicked -> updateProcess(action.process)
            is CoffeeInputAction.OnPlantationValueChanged -> updatePlantation(action.plantation)
            is CoffeeInputAction.OnAltitudeValueChanged -> updateAltitude(action.altitude)
            is CoffeeInputAction.OnScaScoreValueChanged -> updateScaScore(action.scaScore)
            is CoffeeInputAction.OnAdditionalInformationValueChanged -> updateAdditionalInformation(action.additionalInformation)

            is CoffeeInputAction.OnSaveClicked -> saveCoffee()
        }
    }

    private fun back() {
        val newOrUpdatedCoffeeUiState = _uiState.value.newOrUpdatedCoffeeUiState
        if (newOrUpdatedCoffeeUiState.isBlank()) {
            viewModelScope.launch {
                _navigationEvents.emit(CoffeeInputNavigationEvent.NavigateUp)
            }
        } else {
            _uiState.update {
                it.copy(openDiscardDialog = true)
            }
        }
    }

    private fun navigateUp() {
        _uiState.update {
            it.copy(
                openDiscardDialog = false,
                openScaInfoDialog = false
            )
        }
        viewModelScope.launch {
            _navigationEvents.emit(CoffeeInputNavigationEvent.NavigateUp)
        }
    }

    private fun showBottomSheet() {
        _uiState.update {
            it.copy(openBottomSheet = true)
        }
    }

    private fun hideBottomSheet() {
        _uiState.update {
            it.copy(openBottomSheet = false)
        }
    }

    private fun hideDiscardDialog() {
        _uiState.update {
            it.copy(openDiscardDialog = false)
        }
    }

    private fun showScaInfoDialog() {
        _uiState.update {
            it.copy(openScaInfoDialog = true)
        }
    }

    private fun hideScaInfoDialog() {
        _uiState.update {
            it.copy(openScaInfoDialog = false)
        }
    }

    private fun updateImagesUris(uris: List<Uri>) {
        val newOrUpdatedCoffeeUiState = _uiState.value.newOrUpdatedCoffeeUiState
        val selectedCoffeeImages = newOrUpdatedCoffeeUiState.coffeeImages.toMutableList()
        val indexOffset = selectedCoffeeImages.size

        uris.forEachIndexed { index, uri ->
            val coffeeImage = CoffeeImageUiState(
                uri = uri,
                index = index + indexOffset
            )
            selectedCoffeeImages.add(coffeeImage)
        }

        _uiState.update {
            it.copy(newOrUpdatedCoffeeUiState = newOrUpdatedCoffeeUiState.copy(coffeeImages = selectedCoffeeImages))
        }
    }

    private fun showImage(index: Int) {
        _uiState.update {
            it.copy(
                showImage = true,
                selectedImageIndex = index
            )
        }
    }

    private fun updateBrand(brand: String) {
        _uiState.update {
            it.copy(
                newOrUpdatedCoffeeUiState = it.newOrUpdatedCoffeeUiState.copy(roasterOrBrand = brand),
                isRoasterOrBrandError = brand.isBlank()
            )
        }
    }

    private fun updateName(name: String) {
        _uiState.update {
            it.copy(
                newOrUpdatedCoffeeUiState = it.newOrUpdatedCoffeeUiState.copy(originOrName = name),
                isOriginOrNameError = name.isBlank()
            )
        }
    }

    private fun updateAmount(amount: String) {
        _uiState.update {
            it.copy(newOrUpdatedCoffeeUiState = it.newOrUpdatedCoffeeUiState.copy(amount = amount))
        }
    }

    private fun updateRoast(roast: RoastUiState) {
        val selectedRoast = _uiState.value.newOrUpdatedCoffeeUiState.roast
        val updatedRoast = if (selectedRoast == roast) null else roast
        _uiState.update {
            it.copy(newOrUpdatedCoffeeUiState = it.newOrUpdatedCoffeeUiState.copy(roast = updatedRoast))
        }
    }

    private fun updateProcess(process: ProcessUiState) {
        val selectedProcess = _uiState.value.newOrUpdatedCoffeeUiState.process
        val updatedProcess = if (selectedProcess == process) null else process
        _uiState.update {
            it.copy(newOrUpdatedCoffeeUiState = it.newOrUpdatedCoffeeUiState.copy(process = updatedProcess))
        }
    }

    private fun updatePlantation(plantation: String) {
        _uiState.update {
            it.copy(newOrUpdatedCoffeeUiState = it.newOrUpdatedCoffeeUiState.copy(plantation = plantation))
        }
    }

    private fun updateAltitude(altitude: String) {
        _uiState.update {
            it.copy(newOrUpdatedCoffeeUiState = it.newOrUpdatedCoffeeUiState.copy(altitude = altitude))
        }
    }

    private fun updateScaScore(scaScore: String) {
        val scaScoreFloat = scaScore.toFloatOrNull()
        val isScaScoreError = scaScoreFloat == null || scaScoreFloat < 0.0f || scaScoreFloat > 100.0f
        _uiState.update {
            it.copy(
                newOrUpdatedCoffeeUiState = it.newOrUpdatedCoffeeUiState.copy(scaScore = scaScore),
                isScaScoreError = isScaScoreError
            )
        }
    }

    private fun updateAdditionalInformation(additionalInformation: String) {
        _uiState.update {
            it.copy(
                newOrUpdatedCoffeeUiState = it.newOrUpdatedCoffeeUiState.copy(additionalInformation = additionalInformation),
                isAdditionalInformationError = additionalInformation.length > it.additionalInformationMaxLength
            )
        }
    }

    private fun saveCoffee() {
        val oldCoffeeUiState = _uiState.value.oldCoffeeUiState
        val newOrUpdatedCoffeeUiState = _uiState.value.newOrUpdatedCoffeeUiState

        val isOriginOrNameError = newOrUpdatedCoffeeUiState.originOrName.isBlank()
        val isRoasterOrBrandError = newOrUpdatedCoffeeUiState.roasterOrBrand.isBlank()

        _uiState.update {
            it.copy(
                isOriginOrNameError = isOriginOrNameError,
                isRoasterOrBrandError = isRoasterOrBrandError
            )
        }

        if (isOriginOrNameError || isRoasterOrBrandError) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            withContext(Dispatchers.IO) {
                if (_uiState.value.isEdit) {
                    val newCoffeeModel = newOrUpdatedCoffeeUiState.toModel()

                    val oldCoffeeImagesList = oldCoffeeUiState?.coffeeImages ?: emptyList()
                    val newCoffeeImagesList = newOrUpdatedCoffeeUiState.coffeeImages

                    val oldCoffeeImagesSet = oldCoffeeImagesList.toSet()
                    val newCoffeeImagesSet = newCoffeeImagesList.toSet()

                    val coffeeImagesToDeleteList = oldCoffeeImagesSet.subtract(newCoffeeImagesSet).toList()
                    val coffeeImagesToAddList = newCoffeeImagesSet.subtract(oldCoffeeImagesSet).toList()


                } else {
                    val newCoffeeModel = newOrUpdatedCoffeeUiState.toModel()
                    myCoffeeDatabaseRepository.insertCoffee(newCoffeeModel)
                }
            }

            _uiState.update {
                it.copy(isLoading = false)
            }

            _navigationEvents.emit(CoffeeInputNavigationEvent.NavigateUp)
        }
    }

}