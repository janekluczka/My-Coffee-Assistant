package com.luczka.mycoffee.ui.screens.coffees

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.chips.MyCoffeeFilterChip
import com.luczka.mycoffee.ui.components.dialogs.DeleteCoffeeDialog
import com.luczka.mycoffee.ui.components.icons.AddIcon
import com.luczka.mycoffee.ui.components.icons.DeleteIcon
import com.luczka.mycoffee.ui.components.icons.EditIcon
import com.luczka.mycoffee.ui.components.icons.FavoriteIcon
import com.luczka.mycoffee.ui.components.icons.FavoriteOutlinedIcon
import com.luczka.mycoffee.ui.components.listitem.CoffeesListItem
import com.luczka.mycoffee.ui.components.listitem.SwipeableListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeesScreen(
    widthSizeClass: WindowWidthSizeClass,
    uiState: CoffeesUiState,
    onAction: (CoffeesAction) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val filterListState = rememberLazyListState()
    val coffeeListState = rememberLazyListState()

    val fabExpanded by remember {
        derivedStateOf { coffeeListState.firstVisibleItemIndex == 0 }
    }

    Scaffold(
        topBar = {
            when (widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Coffee collection",
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
                    Text(text = stringResource(id = R.string.fab_add_coffee))
                },
                icon = {
                    AddIcon()
                },
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
                onClick = {
                    val action = CoffeesAction.OnAddCoffeeClicked
                    onAction(action)
                },
                expanded = fabExpanded
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is CoffeesUiState.NoCoffees -> {
                NoCoffeesScreen(modifier = Modifier.padding(innerPadding))
            }

            is CoffeesUiState.HasCoffees -> {
                HasCoffeesScreen(
                    coroutineScope = coroutineScope,
                    modifier = Modifier.padding(innerPadding),
                    filterListState = filterListState,
                    coffeeListState = coffeeListState,
                    uiState = uiState,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
private fun NoCoffeesScreen(modifier: Modifier) {
    Column(modifier = modifier) {
        Divider()
        Box(
            modifier = Modifier.fillMaxSize(),
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
                    text = "No coffees added yet",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Add your first coffee by clicking Add coffee button.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HasCoffeesScreen(
    coroutineScope: CoroutineScope,
    modifier: Modifier,
    filterListState: LazyListState,
    coffeeListState: LazyListState,
    uiState: CoffeesUiState.HasCoffees,
    onAction: (CoffeesAction) -> Unit
) {
    LaunchedEffect(uiState.selectedCoffeeFilter) {
        coroutineScope.launch {
            val firstCoffeeNotVisible = coffeeListState.firstVisibleItemIndex != 0

            if (firstCoffeeNotVisible) {
                coffeeListState.animateScrollToItem(index = 0)
            }
        }
    }

    BackHandler {
        if (uiState.selectedCoffeeFilter != CoffeeFilterUiState.All) {
            val action = CoffeesAction.OnFilterClicked(CoffeeFilterUiState.All)
            onAction(action)

            val currentFilterNotVisible = filterListState.firstVisibleItemIndex != 0
            val firstVisibleFilterNotAligned = filterListState.firstVisibleItemScrollOffset != 0

            if (currentFilterNotVisible || firstVisibleFilterNotAligned) {
                coroutineScope.launch {
                    filterListState.animateScrollToItem(index = 0)
                }
            }
        } else {
            val action = CoffeesAction.OnBackClicked
            onAction(action)
        }
    }

    Column(modifier = modifier) {
        LazyRow(
            modifier = Modifier.offset(y = (-8).dp),
            state = filterListState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = uiState.coffeeFilters,
                key = { it.name }
            ) { coffeeFilterUiState ->
                MyCoffeeFilterChip(
                    selected = uiState.selectedCoffeeFilter == coffeeFilterUiState,
                    onClick = {
                        val action = CoffeesAction.OnFilterClicked(coffeeFilterUiState)
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
            state = coffeeListState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = uiState.coffees,
                key = { it.item.coffeeId }
            ) { swipeableCoffeeListItemUiState ->
                val coffeeUiState = swipeableCoffeeListItemUiState.item

                var openDeleteDialog by rememberSaveable { mutableStateOf(false) }

                if(openDeleteDialog) {
                    DeleteCoffeeDialog(
                        coffeeUiState = coffeeUiState,
                        onNegative = {
                            openDeleteDialog = false
                        },
                        onPositive = {
                            openDeleteDialog = false
                            val action = CoffeesAction.OnItemDeleteClicked(coffeeUiState)
                            onAction(action)
                        }
                    )
                }

                SwipeableListItem(
                    isRevealed = swipeableCoffeeListItemUiState.isRevealed,
                    actions = {
                        FilledTonalIconButton(
                            onClick = {
                                val action = CoffeesAction.OnItemIsFavouriteClicked(coffeeUiState)
                                onAction(action)
                            },
                        ) {
                            if (swipeableCoffeeListItemUiState.item.isFavourite) {
                                FavoriteIcon()
                            } else {
                                FavoriteOutlinedIcon()
                            }
                        }
                        FilledTonalIconButton(
                            onClick = {
                                val action = CoffeesAction.OnEditClicked(coffeeId = coffeeUiState.coffeeId)
                                onAction(action)
                            },
                        ) {
                            EditIcon()
                        }
                        FilledTonalIconButton(
                            onClick = {
                                openDeleteDialog = true
                            },
                        ) {
                            DeleteIcon()
                        }
                    },
                    onExpanded = {
                        val action = CoffeesAction.OnItemActionsExpanded(coffeeId = coffeeUiState.coffeeId)
                        onAction(action)
                    },
                    onCollapsed = {
                        val action = CoffeesAction.OnItemActionsCollapsed(coffeeId = coffeeUiState.coffeeId)
                        onAction(action)
                    }
                ) {
                    CoffeesListItem(
                        modifier = Modifier.animateItem(),
                        coffeeUiState = swipeableCoffeeListItemUiState.item,
                        onClick = {
                            val action = CoffeesAction.OnCoffeeClicked(coffeeId = coffeeUiState.coffeeId)
                            onAction(action)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun NoCoffeesScreenPreview() {
    CoffeesScreen(
        widthSizeClass = WindowWidthSizeClass.Compact,
        uiState = CoffeesUiState.NoCoffees,
        onAction = {}
    )
}

@Preview
@Composable
private fun HasCoffeesScreenPreview() {
    CoffeesScreen(
        widthSizeClass = WindowWidthSizeClass.Compact,
        uiState = CoffeesUiState.HasCoffees(
            coffees = listOf(),
            selectedCoffeeFilter = CoffeeFilterUiState.All
        ),
        onAction = {}
    )
}