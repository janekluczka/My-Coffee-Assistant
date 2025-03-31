package com.luczka.mycoffee.ui.screens.coffees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.usecases.DeleteCoffeeUseCase
import com.luczka.mycoffee.domain.usecases.GetCoffeesDataUseCase
import com.luczka.mycoffee.domain.usecases.UpdateCoffeeUseCase
import com.luczka.mycoffee.ui.mappers.toModel
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.models.SwipeableListItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class CoffeesViewModelState(
    val hasCoffees: Boolean = false,
    val selectedCoffeeFilter: CoffeeFilterUiState = CoffeeFilterUiState.All,
    val filteredCoffees: List<SwipeableListItemUiState<CoffeeUiState>> = emptyList(),
) {
    fun toCoffeesUiState(): CoffeesUiState {
        if (hasCoffees) return CoffeesUiState.HasCoffees(
            selectedCoffeeFilter = selectedCoffeeFilter,
            coffees = filteredCoffees
        )

        return CoffeesUiState.NoCoffees
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CoffeesViewModel @Inject constructor(
    private val getCoffeesDataUseCase: GetCoffeesDataUseCase,
    private val updateCoffeeUseCase: UpdateCoffeeUseCase,
    private val deleteCoffeeUseCase: DeleteCoffeeUseCase
) : ViewModel() {

    private val _viewModelState = MutableStateFlow(CoffeesViewModelState())
    val uiState = _viewModelState
        .map(CoffeesViewModelState::toCoffeesUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = _viewModelState.value.toCoffeesUiState()
        )

    private val _oneTimeEvent = MutableSharedFlow<CoffeesOneTimeEvent>()
    val oneTimeEvent = _oneTimeEvent.asSharedFlow()

    init {
        observeCoffeesWhenFilterChanges()
    }

    private fun observeCoffeesWhenFilterChanges() {
        viewModelScope.launch {
            _viewModelState
                .map { viewModelState ->
                    viewModelState.selectedCoffeeFilter
                }
                .distinctUntilChanged()
                .flatMapLatest { coffeeFilterUiState ->
                    getCoffeesDataUseCase(coffeeFilterUiState.toModel())
                }
                .collect { coffeesDataModel ->
                    val filteredCoffees = coffeesDataModel.filteredCoffees.map { SwipeableListItemUiState(item = it.toUiState()) }
                    _viewModelState.update { state ->
                        state.copy(
                            hasCoffees = coffeesDataModel.hasAny,
                            filteredCoffees = filteredCoffees
                        )
                    }
                }
        }
    }


    fun onAction(action: CoffeesAction) {
        when (action) {
            CoffeesAction.OnBackClicked -> navigateUp()
            CoffeesAction.OnAddCoffeeClicked -> navigateToAddCoffee()
            is CoffeesAction.OnEditClicked -> navigateToEdit(action.coffeeId)
            is CoffeesAction.OnCoffeeClicked -> navigateToCoffeeDetails(action.coffeeId)
            is CoffeesAction.OnFilterClicked -> filterChanged(action.filter)
            is CoffeesAction.OnItemActionsExpanded -> collapseOtherItemsActions(action.coffeeId)
            is CoffeesAction.OnItemActionsCollapsed -> collapseItemsActions(action.coffeeId)
            is CoffeesAction.OnItemIsFavouriteClicked -> updateIsFavorite(action.coffeeUiState)
            is CoffeesAction.OnItemDeleteClicked -> deleteCoffee(action.coffeeUiState)
        }
    }

    private fun navigateUp() {
        collapseAllItemsActions()
        viewModelScope.launch {
            _oneTimeEvent.emit(CoffeesOneTimeEvent.NavigateUp)
        }
    }

    private fun navigateToAddCoffee() {
        collapseAllItemsActions()
        viewModelScope.launch {
            _oneTimeEvent.emit(CoffeesOneTimeEvent.NavigateToAddCoffee)
        }
    }

    private fun navigateToEdit(coffeeId: Long) {
        collapseAllItemsActions()
        viewModelScope.launch {
            _oneTimeEvent.emit(CoffeesOneTimeEvent.NavigateToEditCoffee(coffeeId))
        }
    }

    private fun navigateToCoffeeDetails(coffeeId: Long) {
        collapseAllItemsActions()
        viewModelScope.launch {
            _oneTimeEvent.emit(CoffeesOneTimeEvent.NavigateToCoffeeDetails(coffeeId))
        }
    }

    private fun filterChanged(coffeeFilterUiState: CoffeeFilterUiState) {
        _viewModelState.update { viewModelState ->
            viewModelState.copy(selectedCoffeeFilter = coffeeFilterUiState)
        }
    }

    private fun collapseAllItemsActions() {
        _viewModelState.update { viewModelState ->
            viewModelState.copy(
                filteredCoffees = viewModelState.filteredCoffees.map { swipeableListItemUiState ->
                    when {
                        swipeableListItemUiState.isRevealed -> swipeableListItemUiState.copy(isRevealed = false)
                        else -> swipeableListItemUiState
                    }
                }
            )
        }
    }

    private fun collapseOtherItemsActions(coffeeId: Long?) {
        _viewModelState.update { viewModelState ->
            viewModelState.copy(
                filteredCoffees = viewModelState.filteredCoffees.map { swipeableListItemUiState ->
                    when {
                        swipeableListItemUiState.item.coffeeId == coffeeId -> swipeableListItemUiState.copy(isRevealed = true)
                        swipeableListItemUiState.isRevealed -> swipeableListItemUiState.copy(isRevealed = false)
                        else -> swipeableListItemUiState
                    }
                }
            )
        }
    }

    private fun collapseItemsActions(coffeeId: Long) {
        _viewModelState.update { viewModelState ->
            viewModelState.copy(
                filteredCoffees = viewModelState.filteredCoffees.map { swipeableListItemUiState ->
                    when {
                        swipeableListItemUiState.item.coffeeId == coffeeId -> swipeableListItemUiState.copy(isRevealed = false)
                        else -> swipeableListItemUiState
                    }
                }
            )
        }
    }

    private fun updateIsFavorite(coffeeUiState: CoffeeUiState) {
        viewModelScope.launch {
            val updatedCoffeeUiState = coffeeUiState.copy(isFavourite = !coffeeUiState.isFavourite)
            updateCoffeeUseCase(coffeeModel = updatedCoffeeUiState.toModel())
        }
    }

    private fun deleteCoffee(coffeeUiState: CoffeeUiState) {
        viewModelScope.launch {
            deleteCoffeeUseCase(coffeeModel = coffeeUiState.toModel())
        }
    }
}