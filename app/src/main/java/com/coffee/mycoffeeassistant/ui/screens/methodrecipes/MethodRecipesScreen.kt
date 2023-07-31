package com.coffee.mycoffeeassistant.ui.screens.methodrecipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.coffee.mycoffeeassistant.ui.components.RecipeCard
import com.coffee.mycoffeeassistant.ui.model.screens.MethodRecipesUiState
import com.coffee.mycoffeeassistant.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MethodRecipesScreen(
    methodRecipesUiState: MethodRecipesUiState,
    navigateUp: () -> Unit,
    navigate: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { navigateUp() },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    )
                },
                title = {
                    Text(
                        text = methodRecipesUiState.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(methodRecipesUiState.recipesList) { recipeCardUiState ->
                    RecipeCard(
                        recipeCardUiState = recipeCardUiState,
                        onClick = {
                            val route = Screen.RecipeDetails.createRoute(recipeCardUiState.id)
                            navigate(route)
                        }
                    )
                }
            }
        }
    }
}
