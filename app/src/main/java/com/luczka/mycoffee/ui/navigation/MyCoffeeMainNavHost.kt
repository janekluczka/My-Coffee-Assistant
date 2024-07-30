package com.luczka.mycoffee.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.luczka.mycoffee.ui.MyCoffeeViewModelProvider
import com.luczka.mycoffee.ui.screen.assistant.AssistantMainScreen
import com.luczka.mycoffee.ui.screen.assistant.AssistantViewModel
import com.luczka.mycoffee.ui.screen.coffeeinput.CoffeeInputScreen
import com.luczka.mycoffee.ui.screen.coffeeinput.CoffeeInputViewModel

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
            val viewModel: AssistantViewModel = viewModel(
                factory = MyCoffeeViewModelProvider.Factory
            )

            val uiState by viewModel.uiState.collectAsState()

            AssistantMainScreen(
                uiState = uiState,
                navigateUp = mainNavController::navigateUp,
                onCoffeeSelected = viewModel::selectCoffee,
                onUpdateAmountSelectionIntegerPart = viewModel::updateAmountSelectionIntegerPart,
                onUpdateAmountSelectionDecimalPart = viewModel::updateAmountSelectionDecimalPart,
                onUpdateAmountSelectionText = viewModel::updateAmountSelectionValue,
                onUpdateCoffeeAmountSelectionIntegerPart = viewModel::updateAmountSelectionIntegerPart,
                onUpdateCoffeeAmountSelectionDecimalPart = viewModel::updateAmountSelectionDecimalPart,
                onUpdateCoffeeAmountSelectionText = viewModel::updateAmountSelectionValue,
                onUpdateCoffeeRatio = viewModel::updateCoffeeRatioIndex,
                onUpdateWaterRatio = viewModel::updateWaterRatioIndex,
                onUpdateRatioText = viewModel::updateRatioValues,
                onUpdateRating = viewModel::updateRating,
                onUpdateNotes = viewModel::updateNotes,
                onFinishBrew = viewModel::finishBrew
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

            val viewModel: CoffeeInputViewModel = viewModel(
                factory = MyCoffeeViewModelProvider.coffeeInputViewModelFactory(coffeeId)
            )

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            CoffeeInputScreen(
                uiState = uiState,
                navigateUp = mainNavController::navigateUp,
                onSelectPhoto = viewModel::updateImageUri,
                onDeletePhoto = {
                    viewModel.updateImageUri(uri = null)
                    viewModel.updateDeleteImage(deleteImage = true)
                },
                onUpdateName = viewModel::updateName,
                onNameInputFinished = viewModel::nameInputFinished,
                onUpdateBrand = viewModel::updateBrand,
                onBrandInputFinished = viewModel::brandInputFinished,
                onUpdateAmount = viewModel::updateAmount,
                onUpdateScaScore = viewModel::updateScaScore,
                onUpdateProcess = viewModel::updateProcess,
                onUpdateRoast = viewModel::updateRoast,
                onUpdateRoastingDate = viewModel::updateRoastingDate,
                onSave = viewModel::saveCoffee
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