package com.luczka.mycoffee.ui.screens.recipes

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.ui.components.buttons.BackIconButton
import com.luczka.mycoffee.ui.components.listitem.RecipeListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesScreen(
    uiState: RecipesUiState,
    onAction: (RecipesAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackIconButton(
                        onClick = {
                            val action = RecipesAction.NavigateUp
                            onAction(action)
                        }
                    )
                },
                title = {
                    Text(
                        text = uiState.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Divider()
            if (uiState.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            Crossfade(
                targetState = uiState.isLoading,
                label = ""
            ) { isLoading ->
                if (!isLoading) {
                    when (uiState) {
                        is RecipesUiState.HasRecipes -> {
                            if (uiState.recipes.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No recipes at the moment",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            } else {
                                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                    items(
                                        items = uiState.recipes,
                                        key = { it.youtubeId }
                                    ) { recipeCardUiState ->
                                        RecipeListItem(
                                            recipeCardUiState = recipeCardUiState,
                                            onClick = {
                                                val action = RecipesAction.NavigateToRecipeDetails(
                                                    methodId = uiState.methodId,
                                                    recipeId = recipeCardUiState.youtubeId
                                                )
                                                onAction(action)
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        is RecipesUiState.NoRecipes -> {

                        }
                    }
                }
            }
        }
    }
}