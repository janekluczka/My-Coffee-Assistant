package com.luczka.mycoffee.ui.screens.coffees

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
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

    val buttonExpanded by remember {
        derivedStateOf { coffeeListState.firstVisibleItemIndex == 0 }
    }

    when (uiState) {
        is CoffeesUiState.HasCoffees -> {
            LaunchedEffect(uiState.selectedFilter) {
                coroutineScope.launch {
                    scrollToTopIfNewItemsAppeared(coffeeListState = coffeeListState)
                }
            }

            BackHandler {
                if (uiState.selectedFilter != CoffeeFilterUiState.Current) {
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

        is CoffeesUiState.NoCoffees -> {}
    }

    Scaffold(
        topBar = {
            when (widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
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
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(id = R.string.fab_add_coffee)) },
                icon = { Icon(painter = painterResource(id = R.drawable.add_24px), contentDescription = null) },
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
                onClick = {
                    val action = CoffeesAction.NavigateToAddCoffee
                    onAction(action)
                },
                expanded = buttonExpanded
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

private suspend fun scrollToTopIfNewItemsAppeared(coffeeListState: LazyListState) {
    if (coffeeListState.firstVisibleItemIndex != 0) {
        coffeeListState.animateScrollToItem(index = 0)
    }
}

@Composable
private fun NoCoffeesScreen(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
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

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun HasCoffeesScreen(
    innerPadding: PaddingValues,
    filterListState: LazyListState,
    coffeeListState: LazyListState,
    uiState: CoffeesUiState.HasCoffees,
    onAction: (CoffeesAction) -> Unit
) {
    Column(modifier = Modifier.padding(innerPadding)) {
        LazyRow(
            state = filterListState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = uiState.coffeeFilterUiStates,
                key = { it.name }
            ) { coffeeFilter ->
                val isSelected = uiState.selectedFilter == coffeeFilter
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        val action = CoffeesAction.OnSelectedFilterChanged(coffeeFilter)
                        onAction(action)
                    },
                    label = {
                        Text(text = stringResource(id = coffeeFilter.stringRes))
                    },
                    leadingIcon = {
                        Box(
                            modifier = Modifier.animateContentSize(
                                animationSpec = keyframes { durationMillis = 200 }
                            )
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = null,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        }
                    }
                )
            }
        }
        LazyColumn(
            state = coffeeListState,
            contentPadding = PaddingValues(vertical = 8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = uiState.filteredCoffees,
                key = { it.coffeeId }
            ) { coffeeUiState ->
                Box(
                    modifier = Modifier.animateItem(
                        fadeInSpec = null,
                        fadeOutSpec = null,
                        placementSpec = tween()
                    )
                ) {
                    CoffeesListItem(
                        coffeeUiState = coffeeUiState,
                        onClick = {
                            val action = CoffeesAction.NavigateToCoffeeDetails(coffeeUiState.coffeeId)
                            onAction(action)
                        }
                    )
                }
            }
        }
    }
}