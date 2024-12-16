package com.luczka.mycoffee.ui.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.ui.components.icons.AddIcon
import com.luczka.mycoffee.ui.components.listitem.HistoryListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    widthSizeClass: WindowWidthSizeClass,
    uiState: HistoryUiState,
    onAction: (HistoryAction) -> Unit,
) {
    val brewsListState = rememberLazyListState()

    val fabExpanded by remember {
        derivedStateOf { brewsListState.firstVisibleItemIndex == 0 }
    }

    Scaffold(
        topBar = {
            when (widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Brewing history",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(text = "Use assistant")
                },
                icon = {
                    AddIcon()
                },
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
                onClick = {
                    val action = HistoryAction.NavigateToAssistant
                    onAction(action)
                },
                expanded = fabExpanded
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is HistoryUiState.NoBrews -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .padding(32.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No coffees brewed yet",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Brew your first coffee using Assistant.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            is HistoryUiState.HasBrews -> {
                Column(modifier = Modifier.padding(innerPadding)) {
                    Divider()
                    LazyColumn(
                        state = brewsListState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = uiState.brews,
                            key = { it.brewId }
                        ) { brewedUiState ->
                            HistoryListItem(
                                brewUiState = brewedUiState,
                                onClick = {
                                    val action = HistoryAction.NavigateToBrewDetails(brewedUiState.brewId)
                                    onAction(action)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

