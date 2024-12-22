package com.luczka.mycoffee.ui.screens.brewassistant.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.buildSpannedString
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.icons.CloseIcon
import com.luczka.mycoffee.ui.screens.brewassistant.AssistantAction
import com.luczka.mycoffee.ui.screens.brewassistant.AssistantUiState
import com.luczka.mycoffee.ui.screens.brewassistant.dialogs.AssistantAbortDialog
import com.luczka.mycoffee.ui.screens.brewassistant.dialogs.AssistantSaveDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class AssistantPage {
    SELECTION,
    PARAMETERS,
    SUMMARY
}

data class AssistantRecipeCategoryUiState(
    val name: String = "Aeropress",
    val recipes: List<AssistantRecipeUiState> = (1..10).map { AssistantRecipeUiState() }
)

data class AssistantRecipeUiState(
    val name: String = "Aeropress (Original)",
    val coffeeAmountIntegerPart: Int = 15,
    val coffeeAmountFractionalPart: Int = 0,
    val coffeeRatio: Int = 1,
    val waterRatio: Int = 10,
    val waterAmount: String = "150"
)

val categories = (1..3).map { AssistantRecipeCategoryUiState() }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantMainScreen(
    uiState: AssistantUiState,
    onAction: (AssistantAction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = uiState.initialPage,
        pageCount = { uiState.pages.size }
    )

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    var showAbortDialog by rememberSaveable { mutableStateOf(false) }
    var showFinishDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) {
            val action = AssistantAction.NavigateToAssistantRating(brewId = 0L) // TODO: Change to actual value
            onAction(action)
        }
    }

    BackHandler {
        if (pagerState.currentPage == 0) {
            when (uiState) {
                is AssistantUiState.NoneSelected -> {
                    val action = AssistantAction.NavigateUp
                    onAction(action)
                }

                is AssistantUiState.CoffeeSelected -> {
                    if (uiState.selectedCoffees.isEmpty()) {
                        val action = AssistantAction.NavigateUp
                        onAction(action)
                    } else {
                        showAbortDialog = true
                    }
                }
            }
        } else {
            coroutineScope.launch {
                pagerState.animateScrollToPage(
                    page = pagerState.currentPage - 1,
                    animationSpec = tween(500)
                )
            }
        }
    }

    if (showAbortDialog) {
        AssistantAbortDialog(
            onNegative = {
                showAbortDialog = false
            },
            onPositive = {
                showAbortDialog = false
                val action = AssistantAction.NavigateUp
                onAction(action)
            }
        )
    }

    if (showFinishDialog) {
        AssistantSaveDialog(
            uiState = uiState,
            onNegative = {
                showFinishDialog = false
            },
            onPositive = {
                showFinishDialog = false
                val action = AssistantAction.OnFinishButtonClicked
                onAction(action)
            }
        )
    }

    Scaffold(
        topBar = {
            AssistantTopBar(
                pagerState = pagerState,
                uiState = uiState,
                onAction = { action ->
                    when(action) {
                        is AssistantAction.OnShowAbortDialog -> showAbortDialog = true
                        else -> {}
                    }
                    onAction(action)
                }
            )
        },
        bottomBar = {
            AssistantBottomBar(
                pagerState = pagerState,
                coroutineScope = coroutineScope,
                uiState = uiState,
                onAction = { action ->
                    when(action) {
                        is AssistantAction.OnShowFinishDialog -> showFinishDialog = true
                        else -> {}
                    }
                    onAction(action)
                }
            )
        }
    ) { innerPadding ->
        if (showBottomSheet) {
            val bottomSystemBarHeight = with(LocalDensity.current) {
                WindowInsets.systemBars.getBottom(this).toDp()
            }

            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    showBottomSheet = false
                },
                tonalElevation = 0.dp
            ) {
                LazyColumn(contentPadding = PaddingValues(bottom = bottomSystemBarHeight)) {
                    categories.forEachIndexed { index, categoryUiState ->
                        item {
                            val paddingTop = if (index == 0) 0.dp else 24.dp
                            Text(
                                modifier = Modifier.padding(
                                    top = paddingTop,
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 8.dp
                                ),
                                text = categoryUiState.name,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        items(categoryUiState.recipes) {
                            val supportingText = buildSpannedString {
                                append("${it.coffeeAmountIntegerPart}.${it.coffeeAmountFractionalPart} g of coffee")
                                append(" • ")
                                append("${it.coffeeRatio}:${it.waterRatio} ratio")
                                append(" • ")
                                append("${it.waterAmount} of water")
                            }.toString()

                            ListItem(
                                modifier = Modifier.clickable {

                                },
                                headlineContent = {
                                    Text(text = it.name)
                                },
                                supportingContent = {
                                    Text(text = supportingText)
                                }
                            )
                        }
                    }

                }
            }
        }
        AssistantContent(
            innerPadding = innerPadding,
            pagerState = pagerState,
            uiState = uiState,
            onAction = { action ->
                when (action) {
                    is AssistantAction.OnSelectRecipeClicked -> showBottomSheet = true
                    else -> {}
                }
                onAction(action)
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AssistantTopBar(
    pagerState: PagerState,
    uiState: AssistantUiState,
    onAction: (AssistantAction) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = {
                    when (uiState) {
                        is AssistantUiState.NoneSelected -> {
                            val action = if (pagerState.currentPage == 0) {
                                AssistantAction.NavigateUp
                            } else {
                                AssistantAction.OnShowAbortDialog
                            }
                            onAction(action)
                        }

                        is AssistantUiState.CoffeeSelected -> {
                            val action = if (uiState.selectedCoffees.isEmpty()) {
                                AssistantAction.NavigateUp
                            } else {
                                AssistantAction.OnShowAbortDialog
                            }
                            onAction(action)
                        }
                    }
                }
            ) {
                CloseIcon()
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.assistant_top_bar_title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}

@Composable
private fun AssistantContent(
    innerPadding: PaddingValues,
    pagerState: PagerState,
    uiState: AssistantUiState,
    onAction: (AssistantAction) -> Unit
) {
    Column(modifier = Modifier.padding(innerPadding)) {
        Divider(modifier = Modifier.fillMaxWidth())
        HorizontalPager(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            state = pagerState,
            beyondViewportPageCount = 2,
            userScrollEnabled = false
        ) { pageIndex ->
            when (uiState.pages[pageIndex]) {
                AssistantPage.SELECTION -> AssistantSelectionScreen(
                    uiState = uiState,
                    onAction = onAction
                )

                AssistantPage.PARAMETERS -> AssistantParametersScreen(
                    uiState = uiState,
                    onAction = onAction
                )

                AssistantPage.SUMMARY -> AssistantSummaryScreen(
                    uiState = uiState,
                    onAction = onAction
                )
            }
        }
        Divider(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun AssistantBottomBar(
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
    uiState: AssistantUiState,
    onAction: (AssistantAction) -> Unit
) {
    Row(
        modifier = Modifier
            .heightIn(min = 80.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val isFirstPage = pagerState.currentPage == 0
        val isLastPage = pagerState.currentPage ==uiState.pages.lastIndex

        val text = if (isLastPage) {
            stringResource(id = R.string.button_save)
        } else {
            stringResource(id = R.string.button_next)
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            enabled = !isFirstPage,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(
                        page = pagerState.currentPage - 1,
                        animationSpec = tween(500)
                    )
                }
            }
        ) {
            Text(
                text = stringResource(id = R.string.button_previous),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onClick = {
                if (isLastPage) {
                    val action = AssistantAction.OnShowFinishDialog
                    onAction(action)
                } else {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            page = pagerState.currentPage + 1,
                            animationSpec = tween(500)
                        )
                    }
                }
            }
        ) {
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
