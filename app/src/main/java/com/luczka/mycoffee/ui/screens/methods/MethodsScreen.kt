package com.luczka.mycoffee.ui.screens.methods

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.TopAppBarTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MethodsScreen(
    uiState: MethodsUiState,
    navigateToRecipes: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { TopAppBarTitle(text = stringResource(R.string.app_name_short)) }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Crossfade(
                targetState = uiState.isLoading,
                label = ""
            ) { isLoading ->
                if (isLoading) {
                    FullScreenLoadingIndicator()
                } else {
                    when (uiState) {
                        is MethodsUiState.HasMethods -> {
                            HasMethodsScreen(
                                uiState = uiState,
                                navigateToRecipes = navigateToRecipes
                            )
                        }

                        is MethodsUiState.NoMethods -> {

                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FullScreenLoadingIndicator() {

}

@Composable
private fun HasMethodsScreen(
    uiState: MethodsUiState.HasMethods,
    navigateToRecipes: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            items = uiState.methods,
            key = { it.id }
        ) { methodCardUiState ->
            MethodCard(
                methodUiState = methodCardUiState,
                onClick = { navigateToRecipes(methodCardUiState.id) }
            )
        }
    }
}