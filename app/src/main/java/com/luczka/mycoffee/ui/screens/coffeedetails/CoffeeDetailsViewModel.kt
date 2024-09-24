package com.luczka.mycoffee.ui.screens.coffeedetails

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.repository.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.ui.models.CoffeeUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

data class CoffeeDetailsViewModelState(
    val coffeeId: Int,
    val coffee: CoffeeUiState? = null,
    val isDeleted: Boolean = false
) {
    fun toCoffeeDetailsUiState(): CoffeeDetailsUiState {
        return if (coffee == null) {
            CoffeeDetailsUiState.NoCoffee(
                coffeeId = coffeeId,
                isDeleted = isDeleted
            )
        } else {
            CoffeeDetailsUiState.HasCoffee(
                coffee = coffee
            )
        }
    }
}

@AssistedFactory
interface CoffeeDetailsViewModelFactory {
    fun create(coffeeId: Int): CoffeeDetailsViewModel
}

@HiltViewModel(assistedFactory = CoffeeDetailsViewModelFactory::class)
class CoffeeDetailsViewModel @AssistedInject constructor(
    @Assisted coffeeId: Int,
    @ApplicationContext context: Context,
    private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository
) : ViewModel() {

    private val filesDir = context.filesDir

    private val viewModelState = MutableStateFlow(CoffeeDetailsViewModelState(coffeeId = coffeeId))
    val uiState = viewModelState
        .map(CoffeeDetailsViewModelState::toCoffeeDetailsUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toCoffeeDetailsUiState()
        )

    init {
        viewModelScope.launch {
            myCoffeeDatabaseRepository.getCoffeeStream(coffeeId).collect { coffee ->
                viewModelState.update {
                    it.copy(coffee = coffee?.toCoffeeUiState())
                }
            }
        }
    }

    fun onAction(action: CoffeeDetailsAction) {
        when (action) {
            CoffeeDetailsAction.OnFavouriteClicked -> onUpdateFavourite()
            CoffeeDetailsAction.OnDeleteClicked -> onDelete()
            else -> {}
        }
    }

    private fun onUpdateFavourite() {
        val selectedCoffee = viewModelState.value.coffee ?: return

        viewModelScope.launch {
            val updatedCoffee = selectedCoffee.copy(isFavourite = !selectedCoffee.isFavourite)
            myCoffeeDatabaseRepository.updateCoffee(coffeeEntity = updatedCoffee.toCoffee())
        }
    }

    private fun onDelete() {
        val selectedCoffee = viewModelState.value.coffee ?: return

        viewModelScope.launch {
            val imageFile240x240 = selectedCoffee.imageFile240x240
            val imageFile360x360 = selectedCoffee.imageFile360x360
            val imageFile480x480 = selectedCoffee.imageFile480x480
            val imageFile720x720 = selectedCoffee.imageFile720x720
            val imageFile960x960 = selectedCoffee.imageFile960x960

            val file240x240 = imageFile240x240?.let { File(filesDir, it) }
            val file360x360 = imageFile360x360?.let { File(filesDir, it) }
            val file480x480 = imageFile480x480?.let { File(filesDir, it) }
            val file720x720 = imageFile720x720?.let { File(filesDir, it) }
            val file960x960 = imageFile960x960?.let { File(filesDir, it) }

            myCoffeeDatabaseRepository.deleteCoffee(coffeeEntity = selectedCoffee.toCoffee())

            file240x240?.delete()
            file360x360?.delete()
            file480x480?.delete()
            file720x720?.delete()
            file960x960?.delete()
        }
    }

}