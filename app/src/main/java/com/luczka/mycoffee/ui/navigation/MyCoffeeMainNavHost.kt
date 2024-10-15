package com.luczka.mycoffee.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
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
        startDestination = Routes.Main,
    ) {
        composable<Routes.Main> {
            MainRoute(
                widthSizeClass = widthSizeClass,
                nestedNavController = nestedNavController,
                navigateToAssistant = { mainNavController.navigate(Routes.Assistant) },
                navigateToCoffeeInput = { coffeeId ->
                    mainNavController.navigate(Routes.CoffeeInput(coffeeId))
                }
            )
        }
        composable<Routes.Assistant> {
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
        composable<Routes.CoffeeInput> { backStackEntry ->
            val arguments = backStackEntry.toRoute<Routes.CoffeeInput>()
            val viewModel = hiltViewModel<CoffeeInputViewModel, CoffeeInputViewModelFactory> { factory ->
                factory.create(arguments.coffeeId)
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