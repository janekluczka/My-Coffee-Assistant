package com.coffee.mycoffeeassistant.ui.screens.methodrecipes

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coffee.mycoffeeassistant.ui.AppViewModelProvider
import com.coffee.mycoffeeassistant.ui.components.RecipeCard

@Composable
fun MethodRecipesScreen(
    methodId: String,
    navigateToRecipeDetails: (String) -> Unit,
    viewModel: MethodRecipesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val recipeUiStateList = viewModel.recipeUiStateList

    viewModel.getRecipeUiStateList(methodId)

    Crossfade(targetState = recipeUiStateList) { list ->
        if (list.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(24.dp)
            ) {
                items(list) { recipeUiState ->
                    RecipeCard(
                        title = recipeUiState.title,
                        author = recipeUiState.author,
                        videoId = recipeUiState.youtubeId
                    ) {
                        navigateToRecipeDetails(recipeUiState.youtubeId)
                    }
                }
            }
        }
    }

}