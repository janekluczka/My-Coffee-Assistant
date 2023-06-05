package com.coffee.mycoffeeassistant.ui.screens.methods

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coffee.mycoffeeassistant.ui.AppViewModelProvider
import com.coffee.mycoffeeassistant.ui.components.MethodCard

@Composable
fun MethodsScreen(
    navigateToMethodRecipes: (String) -> Unit,
    viewModel: MethodsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val methodUiStateList = viewModel.methodUiStateList

    viewModel.getMethodUiStateList()

    Crossfade(targetState = methodUiStateList) { list ->
        if (list.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(24.dp)
            ) {
                items(list.size) { index ->
                    val methodUiState = list[index]
                    MethodCard(
                        name = methodUiState.name,
                        imageRef = methodUiState.imageRef,
                        onClick = { navigateToMethodRecipes(methodUiState.name.lowercase()) }
                    )
                }
            }
        }
    }
}