package com.coffee.mycoffeeassistant.ui.screens.coffeedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffee.mycoffeeassistant.data.CoffeeRepository
import com.coffee.mycoffeeassistant.ui.model.screens.CoffeeDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class CoffeeDetailsViewModel(
    private val coffeeId: Int,
    private val coffeeRepository: CoffeeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoffeeDetailsUiState())
    val uiState: StateFlow<CoffeeDetailsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            coffeeRepository.getCoffeeStream(id = coffeeId).filterNotNull().collect { coffee ->
                _uiState.update { it.copy(coffeeUiState = coffee.toCoffeeUiState()) }
            }
        }
    }

    fun updateIsFavourite() {
        viewModelScope.launch {
            val updatedIsFavourite = !_uiState.value.coffeeUiState.isFavourite
            val updatedCoffeeUiState =
                _uiState.value.coffeeUiState.copy(isFavourite = updatedIsFavourite)
            coffeeRepository.updateCoffee(updatedCoffeeUiState.toCoffee())
        }
    }

    fun deleteCoffee(filesDir: File, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val coffeeToDelete = _uiState.value.coffeeUiState
            if (coffeeToDelete.hasImage) {
                val imageFile240x240 = coffeeToDelete.imageFile240x240
                val imageFile360x360 = coffeeToDelete.imageFile360x360
                val imageFile480x480 = coffeeToDelete.imageFile480x480
                val imageFile720x720 = coffeeToDelete.imageFile720x720
                val imageFile960x960 = coffeeToDelete.imageFile960x960

                val file240x240 = File(filesDir, imageFile240x240)
                val file360x360 = File(filesDir, imageFile360x360)
                val file480x480 = File(filesDir, imageFile480x480)
                val file720x720 = File(filesDir, imageFile720x720)
                val file960x960 = File(filesDir, imageFile960x960)

                file240x240.delete()
                file360x360.delete()
                file480x480.delete()
                file720x720.delete()
                file960x960.delete()
            }
            coffeeRepository.deleteCoffee(coffeeToDelete.toCoffee())
            onSuccess()
        }
    }

    fun updateCoffeeDetailsUiState(newCoffeeDetailsUiState: CoffeeDetailsUiState) {
        _uiState.update { newCoffeeDetailsUiState.copy() }
    }

}