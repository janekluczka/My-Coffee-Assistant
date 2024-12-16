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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.chips.MyCoffeeFilterChip
import com.luczka.mycoffee.ui.components.icons.AddIcon
import com.luczka.mycoffee.ui.components.icons.DeleteIcon
import com.luczka.mycoffee.ui.components.icons.EditIcon
import com.luczka.mycoffee.ui.components.icons.FavoriteIcon
import com.luczka.mycoffee.ui.components.icons.FavoriteOutlinedIcon
import com.luczka.mycoffee.ui.components.listitem.CoffeesListItem
import com.luczka.mycoffee.ui.components.listitem.SwipeableListItem
import com.luczka.mycoffee.ui.models.CoffeeFilterUiState
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

    when (uiState) {
        is CoffeesUiState.HasCoffees -> {
            LaunchedEffect(uiState.selectedFilter) {
                coroutineScope.launch {
                    scrollToTopIfNewItemsAppeared(listState = coffeeListState)
                }
            }

            BackHandler {
                if (uiState.selectedFilter != CoffeeFilterUiState.All) {
                    val action = CoffeesAction.OnSelectedFilterChanged(CoffeeFilterUiState.All)
                    onAction(action)

                    val currentFilterNotVisible = filterListState.firstVisibleItemIndex != 0
                    val firstVisibleFilterNotAligned = filterListState.firstVisibleItemScrollOffset != 0
                    if (currentFilterNotVisible || firstVisibleFilterNotAligned) {
                        coroutineScope.launch {
                            filterListState.animateScrollToItem(index = 0)
                        }
                    }
                } else {
                    val action = CoffeesAction.NavigateUp
                    onAction(action)
                }
            }
        }

        is CoffeesUiState.NoCoffees -> {

        }
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
                    val action = CoffeesAction.NavigateToAddCoffee
                    onAction(action)
                },
                expanded = fabExpanded
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is CoffeesUiState.NoCoffees -> {
                NoCoffeesScreen(innerPadding = innerPadding)
            }

            is CoffeesUiState.HasCoffees -> {
                HasCoffeesScreen(
                    innerPadding = innerPadding,
                    filterListState = filterListState,
                    coffeeListState = coffeeListState,
                    uiState = uiState,
                    onAction = onAction
                )
            }
        }
    }
}

private suspend fun scrollToTopIfNewItemsAppeared(listState: LazyListState) {
    if (listState.firstVisibleItemIndex != 0) {
        listState.animateScrollToItem(index = 0)
    }
}

@Composable
private fun NoCoffeesScreen(innerPadding: PaddingValues) {
    Column {
        Divider()
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
    innerPadding: PaddingValues,
    filterListState: LazyListState,
    coffeeListState: LazyListState,
    uiState: CoffeesUiState.HasCoffees,
    onAction: (CoffeesAction) -> Unit
) {
    Column(modifier = Modifier.padding(innerPadding)) {
        LazyRow(
            modifier = Modifier.offset(y = (-8).dp),
            state = filterListState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = uiState.coffeeFilterUiStates,
                key = { it.name }
            ) { coffeeFilterUiState ->
                MyCoffeeFilterChip(
                    selected = uiState.selectedFilter == coffeeFilterUiState,
                    onClick = {
                        val action = CoffeesAction.OnSelectedFilterChanged(coffeeFilterUiState)
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
                items = uiState.filteredSwipeableCoffeeListItemUiStates,
                key = { it.coffeeUiState.coffeeId }
            ) { swipeableCoffeeListItemUiState ->
                SwipeableListItem(
                    isRevealed = swipeableCoffeeListItemUiState.isRevealed,
                    actions = {
                        FilledTonalIconButton(
                            onClick = {
                                val action = CoffeesAction.OnFavouriteClicked
                                onAction(action)
                            },
                        ) {
                            if (swipeableCoffeeListItemUiState.coffeeUiState.isFavourite) {
                                FavoriteIcon()
                            } else {
                                FavoriteOutlinedIcon()
                            }
                        }
                        FilledTonalIconButton(
                            onClick = {
                                val action = CoffeesAction.OnEditClicked(coffeeId = swipeableCoffeeListItemUiState.coffeeUiState.coffeeId)
                                onAction(action)
                            },
                        ) {
                            EditIcon()
                        }
                        FilledTonalIconButton(
                            onClick = {
                                val action = CoffeesAction.ShowDeleteDialog
                                onAction(action)
                            },
                        ) {
                            DeleteIcon()
                        }
                    },
                    onExpanded = {
                        val action = CoffeesAction.OnItemActionsExpanded(swipeableCoffeeListItemUiState.coffeeUiState.coffeeId)
                        onAction(action)
                    }
                ) {
                    CoffeesListItem(
                        modifier = Modifier.animateItem(),
                        coffeeUiState = swipeableCoffeeListItemUiState.coffeeUiState,
                        onClick = {
                            val action = if (swipeableCoffeeListItemUiState.isRevealed) {
                                CoffeesAction.OnCollapseItemActions(swipeableCoffeeListItemUiState.coffeeUiState.coffeeId)
                            } else {
                                CoffeesAction.NavigateToCoffeeDetails(swipeableCoffeeListItemUiState.coffeeUiState.coffeeId)
                            }
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
            filteredSwipeableCoffeeListItemUiStates = listOf(),
            selectedFilter = CoffeeFilterUiState.All
        ),
        onAction = {}
    )
}