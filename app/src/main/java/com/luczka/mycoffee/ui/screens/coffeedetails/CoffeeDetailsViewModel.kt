package com.luczka.mycoffee.ui.screens.coffeedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.usecases.DeleteCoffeeUseCase
import com.luczka.mycoffee.domain.usecases.GetCoffeeFlowUseCase
import com.luczka.mycoffee.domain.usecases.UpdateCoffeeUseCase
import com.luczka.mycoffee.ui.mappers.toModel
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.ui.models.CoffeeUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CoffeeDetailsViewModelState(
    val coffeeUiState: CoffeeUiState? = null,
    val isLoading: Boolean = false,
    val openDeleteDialog: Boolean = false
) {
    fun toCoffeeDetailsUiState(): CoffeeDetailsUiState {
        if (coffeeUiState == null) {
            return CoffeeDetailsUiState.NoCoffee(
                isLoading = isLoading,
                openDeleteDialog = openDeleteDialog
            )
        }

        return CoffeeDetailsUiState.HasCoffee(
            coffee = coffeeUiState,
            isLoading = isLoading,
            openDeleteDialog = openDeleteDialog
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
    private val getCoffeeFlowUseCase: GetCoffeeFlowUseCase,
    private val updateCoffeeUseCase: UpdateCoffeeUseCase,
    private val deleteCoffeeUseCase: DeleteCoffeeUseCase,
) : ViewModel() {

    private val viewModelState = MutableStateFlow(CoffeeDetailsViewModelState())
    val uiState = viewModelState
        .map(CoffeeDetailsViewModelState::toCoffeeDetailsUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toCoffeeDetailsUiState()
        )

    private val _navigationEvents = MutableSharedFlow<CoffeeDetailsNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    init {
        viewModelScope.launch {
            getCoffeeFlowUseCase(coffeeId).collect { coffeeModel ->
                viewModelState.update {
                    it.copy(coffeeUiState = coffeeModel?.toUiState())
                }
            }
        }
    }

    fun onAction(action: CoffeeDetailsAction) {
        when (action) {
            is CoffeeDetailsAction.NavigateUp -> navigateUp()
            is CoffeeDetailsAction.OnFavouriteClicked -> updateFavourite()
            is CoffeeDetailsAction.OnEditClicked -> navigateToEdit()
            is CoffeeDetailsAction.ShowDeleteDialog -> showDeleteDialog()
            is CoffeeDetailsAction.HideDeleteDialog -> hideDeleteDialog()
            is CoffeeDetailsAction.OnDeleteClicked -> delete()
        }
    }

    private fun navigateUp() {
        viewModelScope.launch {
            _navigationEvents.emit(CoffeeDetailsNavigationEvent.NavigateUp)
        }
    }

    private fun updateFavourite() {
        val coffeeUiState = viewModelState.value.coffeeUiState ?: return
        viewModelScope.launch {
            val updatedCoffeeUiState = coffeeUiState.copy(isFavourite = !coffeeUiState.isFavourite)
            updateCoffeeUseCase(coffeeModel = updatedCoffeeUiState.toModel())
        }
    }

    private fun navigateToEdit() {
        val coffeeUiState = viewModelState.value.coffeeUiState ?: return
        viewModelScope.launch {
            _navigationEvents.emit(CoffeeDetailsNavigationEvent.NavigateToCoffeeInput(coffeeUiState.coffeeId))
        }
    }

    private fun showDeleteDialog() {
        viewModelState.update {
            it.copy(openDeleteDialog = true)
        }
    }

    private fun hideDeleteDialog() {
        viewModelState.update {
            it.copy(openDeleteDialog = false)
        }
    }

    private fun delete() {
        val coffeeUiState = viewModelState.value.coffeeUiState ?: return

        viewModelScope.launch {
            viewModelState.update {
                it.copy(
                    isLoading = true,
                    openDeleteDialog = false
                )
            }

            deleteCoffeeUseCase(coffeeModel = coffeeUiState.toModel())

            viewModelState.update {
                it.copy(isLoading = false)
            }

            _navigationEvents.emit(CoffeeDetailsNavigationEvent.NavigateUp)
        }
    }

}