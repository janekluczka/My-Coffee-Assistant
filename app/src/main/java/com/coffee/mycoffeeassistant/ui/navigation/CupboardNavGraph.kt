package com.coffee.mycoffeeassistant.ui.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.coffee.mycoffeeassistant.ui.AppViewModelProvider
import com.coffee.mycoffeeassistant.ui.MyCoffeeAssistantAppState
import com.coffee.mycoffeeassistant.ui.screens.addcoffee.AddCoffeeScreen
import com.coffee.mycoffeeassistant.ui.screens.addcoffee.AddCoffeeViewModel
import com.coffee.mycoffeeassistant.ui.screens.coffeedetails.CoffeeDetailsScreen
import com.coffee.mycoffeeassistant.ui.screens.coffeedetails.CoffeeDetailsViewModel
import com.coffee.mycoffeeassistant.ui.screens.cupboard.CupboardScreen
import com.coffee.mycoffeeassistant.ui.screens.cupboard.CupboardViewModel

fun NavGraphBuilder.cupboardNavGraph(appState: MyCoffeeAssistantAppState) {
    navigation(
        startDestination = Screen.Cupboard.route,
        route = "cupboard"
    ) {
        composable(Screen.Cupboard.route) {
            val cupboardViewModel: CupboardViewModel =
                viewModel(factory = AppViewModelProvider.Factory)
            val cupboardUiState by cupboardViewModel.uiState.collectAsStateWithLifecycle()

            CupboardScreen(
                cupboardUiState = cupboardUiState,
                navigate = appState.navController::navigate,
                updateUiState = cupboardViewModel::updateUiState
            )
        }
        composable(Screen.AddCoffee.route) {
            val addCoffeeViewModel: AddCoffeeViewModel =
                viewModel(factory = AppViewModelProvider.Factory)
            val addCoffeeUiState by addCoffeeViewModel.uiState.collectAsStateWithLifecycle()
            val coffeeUiState = addCoffeeViewModel.coffeeUiState

            AddCoffeeScreen(
                addCoffeeUiState = addCoffeeUiState,
                coffeeUiState = coffeeUiState,
                navigateUp = appState.navController::navigateUp,
                updateUiState = addCoffeeViewModel::updateUiState,
                updateCoffeeUiState = addCoffeeViewModel::updateCoffeeUiState,
                saveCoffee = addCoffeeViewModel::saveCoffee
            )
        }
        composable(
            Screen.CupboardCoffeeDetails.route + "/{coffeeId}",
            arguments = listOf(navArgument("coffeeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val coffeeId = backStackEntry.arguments?.getInt("coffeeId")
            if (coffeeId != null) {
                val coffeeDetailsViewModelFactory =
                    AppViewModelProvider.coffeeDetailsViewModelFactory(coffeeId = coffeeId)
                val coffeeDetailsViewModel: CoffeeDetailsViewModel =
                    viewModel(factory = coffeeDetailsViewModelFactory)
                val coffeeDetailsUiState by coffeeDetailsViewModel.uiState.collectAsStateWithLifecycle()

                CoffeeDetailsScreen(
                    coffeeDetailsUiState = coffeeDetailsUiState,
                    navigateUp = appState.navController::navigateUp,
                    updateCoffeeDetailsUiState = coffeeDetailsViewModel::updateCoffeeDetailsUiState,
                    updateIsFavourite = coffeeDetailsViewModel::updateIsFavourite,
                    deleteCoffee = coffeeDetailsViewModel::deleteCoffee
                )
            }
        }
    }
}