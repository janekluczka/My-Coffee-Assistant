package com.coffee.mycoffeeassistant.ui.screens.coffeedetails

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coffee.mycoffeeassistant.ui.AppViewModelProvider
import com.coffee.mycoffeeassistant.ui.components.UserGreeting

@Composable
fun CoffeeDetailsScreen(
    coffeeDetailsViewModel: CoffeeDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coffeeUiState = coffeeDetailsViewModel.coffeeUiState
    coffeeDetailsViewModel.getCoffeeUiState(1)
    UserGreeting(text = coffeeUiState.name)
}