package com.coffee.mycoffeeassistant.ui.screens.cupboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.coffee.mycoffeeassistant.ui.AppViewModelProvider
import com.coffee.mycoffeeassistant.ui.components.CoffeeCardHorizontal
import com.coffee.mycoffeeassistant.ui.components.CoffeeCardVertical
import com.coffee.mycoffeeassistant.ui.navigation.Screen

@Composable
fun CupboardScreen(
    navController: NavController,
    cupboardViewModel: CupboardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val cupboardUiState by cupboardViewModel.uiState.collectAsState()
    val coffeeUiStateList = cupboardViewModel.coffeeUiStateList
    cupboardViewModel.getCoffeeUiStateList()

    Column {
        TabRow(selectedTabIndex = cupboardUiState.state) {
            cupboardUiState.titleResources.forEachIndexed { index, titleResource ->
                Tab(
                    selected = cupboardUiState.state == index,
                    onClick = { cupboardViewModel.updateCupboardUsState(cupboardUiState.copy(state = index)) },
                    text = {
                        Text(
                            text = stringResource(id = titleResource),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
        when (cupboardUiState.state) {
            0 -> LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(24.dp)
            ) {
                items(coffeeUiStateList.size) { index ->
                    val coffeeUiState = coffeeUiStateList[index]
                    CoffeeCardVertical(
                        name = coffeeUiState.name,
                        brand = coffeeUiState.brand,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        navController.navigate(Screen.CoffeeDetails.route + "/${coffeeUiState.id}")
                    }
                }
            }

            1 -> LazyVerticalGrid(
                columns = GridCells.Adaptive(300.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(24.dp)
            ) {
                items(1) { index ->
                    CoffeeCardHorizontal(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        index = index
                    ) {
                        navController.navigate(Screen.CoffeeDetails.route + "/$index")
                    }
                }
            }
        }
    }
}