package com.luczka.mycoffee.ui.screens.coffeedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.ui.mappers.toModel
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.ui.models.CoffeeUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CoffeeDetailsViewModelState(
    val coffeeUiState: CoffeeUiState? = null,
    val isLoading: Boolean = false,
    val isDeleted: Boolean = false
) {
    fun toCoffeeDetailsUiState(): CoffeeDetailsUiState {
        if (coffeeUiState == null) {
            return CoffeeDetailsUiState.NoCoffee(
                isLoading = isLoading,
                isDeleted = isDeleted
            )
        }

        return CoffeeDetailsUiState.HasCoffee(
            coffee = coffeeUiState,
            isLoading = isLoading,
            isDeleted = isDeleted
        )
    }
}

@AssistedFactory
interface CoffeeDetailsViewModelFactory {
    fun create(coffeeId: Long): CoffeeDetailsViewModel
}

@HiltViewModel(assistedFactory = CoffeeDetailsViewModelFactory::class)
class CoffeeDetailsViewModel @AssistedInject constructor(
    @Assisted coffeeId: Long,
    private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(CoffeeDetailsViewModelState())
    val uiState = viewModelState
        .map(CoffeeDetailsViewModelState::toCoffeeDetailsUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toCoffeeDetailsUiState()
        )

    init {
        viewModelScope.launch {
            myCoffeeDatabaseRepository.getCoffeeFlow(coffeeId).collect { coffeeModel ->
                viewModelState.update {
                    it.copy(coffeeUiState = coffeeModel?.toUiState())
                }
            }
        }
    }

    fun onAction(action: CoffeeDetailsAction) {
        when (action) {
            CoffeeDetailsAction.OnFavouriteClicked -> updateFavourite()
            CoffeeDetailsAction.OnDeleteClicked -> delete()
            else -> {}
        }
    }

    private fun updateFavourite() {
        val coffeeUiState = viewModelState.value.coffeeUiState ?: return
        viewModelScope.launch {
            val updatedCoffeeUiState = coffeeUiState.copy(isFavourite = !coffeeUiState.isFavourite)
            myCoffeeDatabaseRepository.updateCoffee(coffeeModel = updatedCoffeeUiState.toModel())
        }
    }

    private fun delete() {
        val coffeeUiState = viewModelState.value.coffeeUiState ?: return

        viewModelScope.launch {
            viewModelState.update {
                it.copy(isLoading = true)
            }

            myCoffeeDatabaseRepository.deleteCoffee(coffeeModel = coffeeUiState.toModel())

            viewModelState.update {
                it.copy(
                    isLoading = false,
                    isDeleted = true
                )
            }
        }
    }

}