package com.coffee.mycoffeeassistant.ui.screens.cupboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.coffee.mycoffeeassistant.R
import com.coffee.mycoffeeassistant.ui.components.HorizontalCoffeeAlternativeCard
import com.coffee.mycoffeeassistant.ui.components.VerticalCoffeeCard
import com.coffee.mycoffeeassistant.ui.model.components.CoffeeCardUiState
import com.coffee.mycoffeeassistant.ui.model.screens.CupboardUiState
import com.coffee.mycoffeeassistant.ui.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CupboardScreen(
    cupboardUiState: CupboardUiState,
    navigate: (String) -> Unit,
    updateUiState: (CupboardUiState) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState()

    val tabTitleResources: List<Int> = listOf(
        R.string.tab_cupboard_my_stock,
        R.string.tab_cupboard_my_favourites
    )

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
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(id = R.string.fab_cupboard_add_coffee)) },
                icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = null) },
                onClick = { navigate(Screen.AddCoffee.route) },
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TabRow(
                selectedTabIndex = cupboardUiState.currentTab,
                tabs = {
                    tabTitleResources.forEachIndexed { index, tabTitleResource ->
                        Tab(
                            selected = cupboardUiState.currentTab == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(page = index)
                                }
                                updateUiState(cupboardUiState.copy(currentTab = index))
                            },
                            text = {
                                Text(
                                    text = stringResource(id = tabTitleResource),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )
                    }
                }
            )
            HorizontalPager(
                pageCount = tabTitleResources.size,
                beyondBoundsPageCount = 2,
                state = pagerState,
                userScrollEnabled = false,
            ) { page ->
                when (page) {
                    0 -> VerticalCoffeeCardGrid(
                        coffeeUiStateList = cupboardUiState.inStockCoffeeUiStateList,
                        navigate = navigate,
                    )

                    1 -> HorizontalCoffeeCardGrid(
                        coffeeUiStateList = cupboardUiState.favouriteCoffeeUiStateList,
                        navigate = navigate,
                    )
                }
            }
        }
    }
}

@Composable
private fun VerticalCoffeeCardGrid(
    coffeeUiStateList: List<CoffeeCardUiState>,
    navigate: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(coffeeUiStateList, { it.id }) { coffeeUiState ->
            VerticalCoffeeCard(
                onClick = {
                    val route = Screen.CupboardCoffeeDetails.createRoute(id = coffeeUiState.id)
                    navigate(route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                coffeeCardUiState = coffeeUiState
            )
        }
    }
}

@Composable
private fun HorizontalCoffeeCardGrid(
    coffeeUiStateList: List<CoffeeCardUiState>,
    navigate: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(coffeeUiStateList, { it.id }) { coffeeUiState ->
            HorizontalCoffeeAlternativeCard(
                name = coffeeUiState.name,
                brand = coffeeUiState.brand,
                imageFile = coffeeUiState.imageFile240x240,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onClick = {
                    val route = Screen.CupboardCoffeeDetails.createRoute(id = coffeeUiState.id)
                    navigate(route)
                }
            )
        }
    }
}