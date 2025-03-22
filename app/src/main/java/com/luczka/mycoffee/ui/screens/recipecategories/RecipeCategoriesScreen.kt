package com.luczka.mycoffee.ui.screens.recipecategories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.cards.MethodCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCategoriesScreen(
    widthSizeClass: WindowWidthSizeClass,
    uiState: RecipeCategoriesUiState,
    onAction: (RecipeCategoriesAction) -> Unit,
) {
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isError) {
        if (uiState.isError) {
            val result = snackbarHostState.showSnackbar(
                message = context.getString(uiState.errorMessageRes),
                actionLabel = context.getString(R.string.action_retry),
                duration = SnackbarDuration.Indefinite,
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    val action = RecipeCategoriesAction.OnRetryClicked
                    onAction(action)
                }

                SnackbarResult.Dismissed -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            when (widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Recipe categories",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column {
                Divider()
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Adaptive(150.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        if (uiState is RecipeCategoriesUiState.HasRecipeCategories) {
                            items(
                                items = uiState.categories,
                                key = { it.id }
                            ) { methodCardUiState ->
                                MethodCard(
                                    modifier = Modifier.animateItem(),
                                    categoryUiState = methodCardUiState,
                                    onClick = {
                                        val action = RecipeCategoriesAction.NavigateToRecipes(methodCardUiState)
                                        onAction(action)
                                    }
                                )
                            }
                        }
                    }
                    if (uiState is RecipeCategoriesUiState.NoRecipeCategories && !uiState.isLoading && !uiState.isError) {
                        Text(
                            modifier = Modifier.padding(32.dp),
                            text = "No categories at the moment",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            if (uiState.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}