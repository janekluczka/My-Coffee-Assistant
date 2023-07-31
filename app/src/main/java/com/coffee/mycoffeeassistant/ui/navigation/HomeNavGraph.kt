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
import com.coffee.mycoffeeassistant.ui.screens.brewassistant.BrewAssistantScreen
import com.coffee.mycoffeeassistant.ui.screens.brewassistant.BrewAssistantViewModel
import com.coffee.mycoffeeassistant.ui.screens.coffeedetails.CoffeeDetailsScreen
import com.coffee.mycoffeeassistant.ui.screens.coffeedetails.CoffeeDetailsViewModel
import com.coffee.mycoffeeassistant.ui.screens.home.HomeScreen
import com.coffee.mycoffeeassistant.ui.screens.home.HomeViewModel

fun NavGraphBuilder.homeNavGraph(appState: MyCoffeeAssistantAppState) {
    navigation(
        startDestination = Screen.Home.route,
        route = "home"
    ) {
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()

            HomeScreen(
                homeUiState = homeUiState,
                navigate = appState.navController::navigate
            )
        }
        composable(Screen.BrewAssistant.route) {
            val navController = appState.navController
            val brewAssistantViewModel: BrewAssistantViewModel =
                viewModel(factory = AppViewModelProvider.Factory)
            val brewAssistantUiState by brewAssistantViewModel.uiState.collectAsStateWithLifecycle()

            BrewAssistantScreen(
                brewAssistantUiState = brewAssistantUiState,
                navigateUp = navController::navigateUp,
            )
        }
        composable(
            Screen.HomeCoffeeDetails.route + "/{coffeeId}",
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