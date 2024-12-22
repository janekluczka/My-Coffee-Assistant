package com.luczka.mycoffee.ui.screens.brews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.ui.components.chips.MyCoffeeFilterChip
import com.luczka.mycoffee.ui.components.dialogs.DeleteBrewDialog
import com.luczka.mycoffee.ui.components.icons.AddIcon
import com.luczka.mycoffee.ui.components.icons.DeleteIcon
import com.luczka.mycoffee.ui.components.listitem.BrewListItem
import com.luczka.mycoffee.ui.components.listitem.SwipeableListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrewsScreen(
    widthSizeClass: WindowWidthSizeClass,
    uiState: BrewsUiState,
    onAction: (BrewsAction) -> Unit,
) {
    val filterListState = rememberLazyListState()
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
                    val action = BrewsAction.NavigateToAssistant
                    onAction(action)
                },
                expanded = fabExpanded
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is BrewsUiState.NoBrews -> {
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

            is BrewsUiState.HasBrews -> {
                Column(modifier = Modifier.padding(innerPadding)) {
                    LazyRow(
                        modifier = Modifier.offset(y = (-8).dp),
                        state = filterListState,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = uiState.brewFilters,
                            key = { it.name }
                        ) { coffeeFilterUiState ->
                            MyCoffeeFilterChip(
                                selected = uiState.selectedFilter == coffeeFilterUiState,
                                onClick = {
                                    val action = BrewsAction.OnSelectedFilterChanged(coffeeFilterUiState)
                                    onAction(action)
                                },
                                label = {
                                    Text(text = stringResource(id = coffeeFilterUiState.stringRes))
                                }
                            )
                        }
                    }
                    Divider()
                    LazyColumn(
                        state = brewsListState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = uiState.brews,
                            key = { it.item.brewId }
                        ) { swipeableListItemUiState ->
                            val brewUiState = swipeableListItemUiState.item
                            val brewId = brewUiState.brewId

                            var openDeleteDialog by rememberSaveable { mutableStateOf(false) }

                            if(openDeleteDialog) {
                                DeleteBrewDialog(
                                    brewUiState = brewUiState,
                                    onNegative = {
                                        openDeleteDialog = false
                                    },
                                    onPositive = {
                                        openDeleteDialog = false
                                        val action = BrewsAction.OnDeleteClicked(brewId = brewId)
                                        onAction(action)
                                    }
                                )
                            }

                            SwipeableListItem(
                                isRevealed = swipeableListItemUiState.isRevealed,
                                actions = {
                                    FilledTonalIconButton(
                                        onClick = {
                                            openDeleteDialog = true
                                        },
                                    ) {
                                        DeleteIcon()
                                    }
                                },
                                onExpanded = {
                                    val action = BrewsAction.OnItemActionsExpanded(brewId = brewId)
                                    onAction(action)
                                },
                                onCollapsed = {
                                    val action = BrewsAction.OnItemActionsCollapsed(brewId = brewId)
                                    onAction(action)
                                }
                            ) {
                                BrewListItem(
                                    brewUiState = brewUiState,
                                    onClick = {
                                        val action = BrewsAction.NavigateToBrewDetails(brewId)
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
}

