package com.coffee.mycoffeeassistant.ui.screens.cupboard

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coffee.mycoffeeassistant.ui.AppViewModelProvider
import com.coffee.mycoffeeassistant.ui.components.HorizontalCoffeeCard
import com.coffee.mycoffeeassistant.ui.components.VerticalCoffeeCard
import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CupboardScreen(
    navigateToDetails: (Int) -> Unit,
    viewModel: CupboardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val cupboardUiState by viewModel.uiState.collectAsState()

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            viewModel.updateCurrentTab(currentTab = it)
        }
    }

    Column {
        TabRow(selectedTabIndex = cupboardUiState.currentTab) {
            cupboardUiState.tabTitleResources.forEachIndexed { index, tabTitleResource ->
                Tab(
                    selected = cupboardUiState.currentTab == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                        viewModel.updateCurrentTab(currentTab = index)
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
        HorizontalPager(
            pageCount = cupboardUiState.tabTitleResources.size,
            beyondBoundsPageCount = 2,
            state = pagerState,
            userScrollEnabled = false,
        ) { page ->
            when (page) {
                0 -> VerticalCoffeeCardGrid(
                    coffeeUiStateList = viewModel.allCoffeeUiStateList,
                    navigateToDetails = navigateToDetails
                )

                1 -> HorizontalCoffeeCardGrid(
                    coffeeUiStateList = viewModel.favouriteCoffeeUiStateList,
                    navigateToDetails = navigateToDetails
                )
            }
        }

    }
}

@Composable
private fun VerticalCoffeeCardGrid(
    coffeeUiStateList: List<CoffeeUiState>,
    navigateToDetails: (Int) -> Unit
) {
    Crossfade(targetState = coffeeUiStateList) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
        ) {
            items(it) { coffeeUiState ->
                VerticalCoffeeCard(
                    name = coffeeUiState.name,
                    brand = coffeeUiState.brand,
                    bitmap = coffeeUiState.bitmap,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    navigateToDetails(coffeeUiState.id)
                }
            }
        }
    }
}

@Composable
private fun HorizontalCoffeeCardGrid(
    coffeeUiStateList: List<CoffeeUiState>,
    navigateToDetails: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
    ) {
        items(coffeeUiStateList) { coffeeUiState ->
            HorizontalCoffeeCard(
                name = coffeeUiState.name,
                brand = coffeeUiState.brand,
                bitmap = coffeeUiState.bitmap,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                navigateToDetails(coffeeUiState.id)
            }
        }
    }
}