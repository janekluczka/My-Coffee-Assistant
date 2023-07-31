package com.coffee.mycoffeeassistant.ui.screens.methods

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.coffee.mycoffeeassistant.R
import com.coffee.mycoffeeassistant.ui.components.MethodCard
import com.coffee.mycoffeeassistant.ui.model.screens.MethodsUiState
import com.coffee.mycoffeeassistant.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MethodsScreen(
    methodsUiState: MethodsUiState,
    navigate: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name_short),
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
                items(methodsUiState.methodsList) { methodCardUiState ->
                    MethodCard(
                        methodCardUiState = methodCardUiState,
                        onClick = {
                            val route = Screen.MethodRecipes.createRoute(methodCardUiState.id)
                            navigate(route)
                        }
                    )
                }
            }
        }
    }
}