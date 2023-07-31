package com.coffee.mycoffeeassistant.ui.screens.brewassistant

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import com.coffee.mycoffeeassistant.ui.model.screens.BrewAssistantUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrewAssistantScreen(
    brewAssistantUiState: BrewAssistantUiState,
    navigateUp: () -> Unit,
) {
    val context = LocalContext.current

    // TODO: Add view model functions, change dropdown menu
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { navigateUp() },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null
                            )
                        }
                    )
                },
                title = {
                    Text(
                        "Brew assistant",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

        }
    }
}