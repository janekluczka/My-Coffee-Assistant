package com.luczka.mycoffee.ui.screens.coffees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.models.CoffeeModel
import com.luczka.mycoffee.domain.usecases.DeleteCoffeeUseCase
import com.luczka.mycoffee.domain.usecases.GetAllCoffeesFlowUseCase
import com.luczka.mycoffee.domain.usecases.UpdateCoffeeUseCase
import com.luczka.mycoffee.ui.mappers.toModel
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.models.SwipeableListItemUiState
import com.luczka.mycoffee.ui.screens.coffees.filter.CoffeeFilterStrategy
import com.luczka.mycoffee.ui.screens.coffees.filter.FilterAllStrategy
import com.luczka.mycoffee.ui.screens.coffees.filter.FilterCurrentStrategy
import com.luczka.mycoffee.ui.screens.coffees.filter.FilterFavouritesStrategy
import com.luczka.mycoffee.ui.screens.coffees.filter.FilterLowStrategy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class CoffeesViewModelState(
    val coffees: List<CoffeeUiState> = emptyList(),
    val selectedCoffeeFilter: CoffeeFilterUiState = CoffeeFilterUiState.All,
    val filteredCoffees: List<SwipeableListItemUiState<CoffeeUiState>> = emptyList(),
) {
    fun toCoffeesUiState(): CoffeesUiState {
        if (coffees.isEmpty()) return CoffeesUiState.NoCoffees

        return CoffeesUiState.HasCoffees(
            selectedCoffeeFilter = selectedCoffeeFilter,
            coffees = filteredCoffees
        )
    }
}

@HiltViewModel
class CoffeesViewModel @Inject constructor(
    private val getAllCoffeesFlowUseCase: GetAllCoffeesFlowUseCase,
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
        viewModelScope.launch {
            getAllCoffeesFlowUseCase()
                .map(List<CoffeeModel>::toUiState)
                .collect {
                    updateCoffeesAndFilter(
                        coffees = it,
                        filter = null
                    )
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
        updateCoffeesAndFilter(
            coffees = null,
            filter = coffeeFilterUiState
        )
    }

    private fun updateCoffeesAndFilter(
        coffees: List<CoffeeUiState>?,
        filter: CoffeeFilterUiState?
    ) {
        _viewModelState.update { viewModelState ->
            val updatedCoffees = coffees ?: viewModelState.coffees
            val updatedFilter = filter ?: viewModelState.selectedCoffeeFilter
            val filteredCoffees = filterCoffees(
                coffees = updatedCoffees,
                filter = updatedFilter
            )
            viewModelState.copy(
                coffees = updatedCoffees,
                selectedCoffeeFilter = updatedFilter,
                filteredCoffees = filteredCoffees.map { coffeeUiState -> SwipeableListItemUiState(item = coffeeUiState) }
            )
        }
    }

    private fun filterCoffees(
        coffees: List<CoffeeUiState>,
        filter: CoffeeFilterUiState
    ): List<CoffeeUiState> {
        val filterStrategy: CoffeeFilterStrategy = when (filter) {
            CoffeeFilterUiState.All -> FilterAllStrategy()
            CoffeeFilterUiState.Current -> FilterCurrentStrategy()
            CoffeeFilterUiState.Favourites -> FilterFavouritesStrategy()
            CoffeeFilterUiState.Low -> FilterLowStrategy()
        }
        return filterStrategy.filter(coffees)
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