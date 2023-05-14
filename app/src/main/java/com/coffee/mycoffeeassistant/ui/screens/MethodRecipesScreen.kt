package com.coffee.mycoffeeassistant.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.coffee.mycoffeeassistant.ui.components.RecipeCard
import com.coffee.mycoffeeassistant.ui.navigation.Screen

@Composable
fun MethodRecipesScreen(navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(24.dp)
    ) {
        items(1) { index ->
            RecipeCard(
                title = "",
                author = ""
            ) {
                navController.navigate(Screen.RecipeDetails.route)
            }
        }
    }
}