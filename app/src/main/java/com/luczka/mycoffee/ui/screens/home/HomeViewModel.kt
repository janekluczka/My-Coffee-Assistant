package com.luczka.mycoffee.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.ui.models.BrewUiState
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.navigation.MainNavHostRoute
import com.luczka.mycoffee.ui.navigation.MainNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class HomeViewModelState(
    val recentBrews: List<BrewUiState> = emptyList(),
    val recentlyAddedCoffees: List<CoffeeUiState> = emptyList(),
) {
    fun toHomeUiState(): HomeUiState {
        return HomeUiState.HasCoffees(
            recentBrews = recentBrews,
            recentlyAddedCoffees = recentlyAddedCoffees
        )
    }
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository,
    private val mainNavigator: MainNavigator
) : ViewModel() {

    private val viewModelState = MutableStateFlow(HomeViewModelState())
    val uiState = viewModelState
        .map(HomeViewModelState::toHomeUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toHomeUiState()
        )

    init {
        viewModelScope.launch {
            myCoffeeDatabaseRepository.getRecentlyAddedCoffeesFlow(amount = 5).collect { coffeeModels ->
                val recentlyAddedCoffees = coffeeModels.map { it.toUiState() }
                viewModelState.update {
                    it.copy(recentlyAddedCoffees = recentlyAddedCoffees)
                }
            }
        }
        viewModelScope.launch {
            myCoffeeDatabaseRepository.getRecentBrewsFlow(amount = 5).collect { brewModels ->
                val recentBrews = brewModels.map { it.toUiState() }
                viewModelState.update {
                    it.copy(recentBrews = recentBrews)
                }
            }
        }
    }

    fun onAction(action: HomeAction) {
        when(action) {
            is HomeAction.OnMenuClicked -> {}
            is HomeAction.NavigateToBrewAssistant -> navigateToAssistant()
            is HomeAction.NavigateToBrewDetails -> navigateToBrewDetails(action.brewId)
            is HomeAction.NavigateToCoffeeDetails -> navigateToCoffeeDetails(action.coffeeId)
        }
    }

    private fun navigateToAssistant() {
        viewModelScope.launch {
            mainNavigator.navigate(MainNavHostRoute.BrewAssistant)
        }
    }

    private fun navigateToBrewDetails(brewId: Long) {
        viewModelScope.launch {
            mainNavigator.navigate(MainNavHostRoute.BrewDetails(brewId))
        }
    }

    private fun navigateToCoffeeDetails(coffeeId: Long) {
        viewModelScope.launch {
            mainNavigator.navigate(MainNavHostRoute.CoffeeDetails(coffeeId))
        }
    }

}