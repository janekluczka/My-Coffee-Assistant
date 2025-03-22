package com.luczka.mycoffee.ui.screens.recipelist

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.luczka.mycoffee.ui.components.dialogs.MethodInfoDialog
import com.luczka.mycoffee.ui.components.icons.ArrowBackIcon
import com.luczka.mycoffee.ui.components.icons.InfoIcon
import com.luczka.mycoffee.ui.components.listitem.RecipeListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesListScreen(
    uiState: RecipeListUiState,
    onAction: (RecipeListAction) -> Unit,
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
                    val action = RecipeListAction.OnRetryClicked
                    onAction(action)
                }

                SnackbarResult.Dismissed -> {}
            }
        }
    }

    if (uiState.openMethodInfoDialog) {
        MethodInfoDialog(
            method = uiState.categoryUiState,
            onDismiss = {
                val action = RecipeListAction.HideMethodInfoDialog
                onAction(action)
            }
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            val action = RecipeListAction.NavigateUp
                            onAction(action)
                        }
                    ) {
                        ArrowBackIcon()
                    }
                },
                title = {
                    Text(
                        text = uiState.categoryUiState.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    if (uiState.hasInfoButton) {
                        IconButton(
                            onClick = {
                                val action = RecipeListAction.ShowMethodInfoDialog
                                onAction(action)
                            }
                        ) {
                            InfoIcon()
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column {
                Divider()
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        if (uiState is RecipeListUiState.HasRecipes) {
                            items(
                                items = uiState.recipes,
                                key = { it.youtubeId }
                            ) { recipeCardUiState ->
                                RecipeListItem(
                                    modifier = Modifier.animateItem(),
                                    recipeCardUiState = recipeCardUiState,
                                    onClick = {
                                        val action = RecipeListAction.NavigateToRecipeDetails(recipeUiState = recipeCardUiState)
                                        onAction(action)
                                    }
                                )
                            }
                        }
                    }
                    if (uiState is RecipeListUiState.NoRecipes && !uiState.isLoading && !uiState.isError) {
                        Text(
                            modifier = Modifier.padding(32.dp),
                            text = "No recipes at the moment",
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