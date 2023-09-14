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
import com.luczka.mycoffee.ui.screens.brewassistant.AssistantMainScreen
import com.luczka.mycoffee.ui.screens.brewassistant.AssistantViewModel
import com.luczka.mycoffee.ui.screens.coffeeinput.CoffeeInputScreen
import com.luczka.mycoffee.ui.screens.coffeeinput.CoffeeInputViewModel

@Composable
fun MyCoffeeMainNavHost(
    widthSizeClass: WindowWidthSizeClass,
    mainNavController: NavHostController,
    navController: NavHostController
) {
    NavHost(
        navController = mainNavController,
        startDestination = MyCoffeeDestinations.ROUTE_MAIN,
    ) {
        composable(MyCoffeeDestinations.ROUTE_MAIN) {
            MainRoute(
                widthSizeClass = widthSizeClass,
                navController = navController,
                navigateToAssistant = {
                    val route = MyCoffeeDestinations.ROUTE_ASSISTANT
                    mainNavController.navigate(route = route)
                },
                navigateToAddCoffee = {
                    val route = MyCoffeeDestinations.ROUTE_INPUT
                    mainNavController.navigate(route = route)
                },
                navigateToEditCoffee = { coffeeId ->
                    val route =
                        "${MyCoffeeDestinations.ROUTE_INPUT}?${MyCoffeeDestinations.KEY_COFFEE_ID}=${coffeeId}"
                    mainNavController.navigate(route = route)
                }
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
                onUpdateAmountSelectionWholeNumber = viewModel::updateAmountSelectionWholeNumber,
                onUpdateAmountSelectionFractionalPart = viewModel::updateAmountSelectionFractionalPart,
                onUpdateAmountSelectionText = viewModel::updateAmountSelectionText,
                onUpdateHasRatio = viewModel::updateHasRatio,
                onUpdateCoffeeRatio = viewModel::updateCoffeeRatioIndex,
                onUpdateWaterRatio = viewModel::updateWaterRatioIndex,
                onUpdateRatioText = viewModel::updateRatioText,
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
                onPhotoSelected = viewModel::updateImageUri,
                onPhotoDeleted = {
                    viewModel.updateImageUri(uri = null)
                    viewModel.updateDeleteImage(deleteImage = true)
                },
                onUpdateName = viewModel::updateName,
                onNameInputFinished = viewModel::nameInputFinished,
                onUpdateBrand = viewModel::updateBrand,
                onBrandInputFinished = viewModel::brandInputFinished,
                onUpdateAmount = viewModel::updateAmount,
                onUpdateProcess = viewModel::updateProcess,
                onUpdateRoast = viewModel::updateRoast,
                onUpdateRoastingDate = viewModel::updateRoastingDate,
                onSave = viewModel::saveCoffee
            )
        }
    }
}