package com.luczka.mycoffee.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.luczka.mycoffee.ui.screens.assistant.AssistantAction
import com.luczka.mycoffee.ui.screens.assistant.AssistantViewModel
import com.luczka.mycoffee.ui.screens.assistant.screens.AssistantMainScreen
import com.luczka.mycoffee.ui.screens.coffeeinput.CoffeeInputAction
import com.luczka.mycoffee.ui.screens.coffeeinput.CoffeeInputScreen
import com.luczka.mycoffee.ui.screens.coffeeinput.CoffeeInputViewModel
import com.luczka.mycoffee.ui.screens.coffeeinput.CoffeeInputViewModelFactory

@Composable
fun MyCoffeeMainNavHost(
    widthSizeClass: WindowWidthSizeClass,
    mainNavController: NavHostController,
    nestedNavController: NavHostController
) {
    NavHost(
        navController = mainNavController,
        startDestination = MyCoffeeDestinations.ROUTE_MAIN,
    ) {
        composable(MyCoffeeDestinations.ROUTE_MAIN) {
            NestedRoute(
                widthSizeClass = widthSizeClass,
                nestedNavController = nestedNavController,
                navigateToAssistant = mainNavController::navigateToAssistant,
                navigateToAddCoffee = mainNavController::navigateToAddCoffee,
                navigateToEditCoffee = mainNavController::navigateToEditCoffee
            )
        }
        composable(MyCoffeeDestinations.ROUTE_ASSISTANT) {
            val viewModel = hiltViewModel<AssistantViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            AssistantMainScreen(
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is AssistantAction.NavigateUp -> mainNavController.navigateUp()
                        else -> {}
                    }
                    viewModel.onAction(action)
                }
            )
        }
        composable(
            route = "${MyCoffeeDestinations.ROUTE_INPUT}?${MyCoffeeDestinations.KEY_COFFEE_ID}={${MyCoffeeDestinations.KEY_COFFEE_ID}}",
            arguments = listOf(
                navArgument(
                    name = MyCoffeeDestinations.KEY_COFFEE_ID,
                    builder = {
                        type = NavType.StringType
                        nullable = true
                    }
                )
            )
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val coffeeId = arguments?.getString(MyCoffeeDestinations.KEY_COFFEE_ID)?.toIntOrNull()

            val viewModel = hiltViewModel<CoffeeInputViewModel, CoffeeInputViewModelFactory> { factory ->
                factory.create(coffeeId)
            }

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            CoffeeInputScreen(
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is CoffeeInputAction.NavigateUp -> mainNavController.navigateUp()
                        else -> {}
                    }
                    viewModel.onAction(action)
                }
            )
        }
    }
}

private fun NavHostController.navigateToEditCoffee(coffeeId: Int) {
    this.navigate(route = "${MyCoffeeDestinations.ROUTE_INPUT}?${MyCoffeeDestinations.KEY_COFFEE_ID}=${coffeeId}")
}

private fun NavHostController.navigateToAssistant() {
    this.navigate(route = MyCoffeeDestinations.ROUTE_ASSISTANT)
}

private fun NavHostController.navigateToAddCoffee() {
    this.navigate(route = MyCoffeeDestinations.ROUTE_INPUT)
}