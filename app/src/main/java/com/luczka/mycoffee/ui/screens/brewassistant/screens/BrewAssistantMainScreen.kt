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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.buildSpannedString
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.icons.CloseIcon
import com.luczka.mycoffee.ui.screens.brewassistant.BrewAssistantAction
import com.luczka.mycoffee.ui.screens.brewassistant.BrewAssistantPage
import com.luczka.mycoffee.ui.screens.brewassistant.BrewAssistantUiState
import com.luczka.mycoffee.ui.screens.brewassistant.dialogs.BrewAssistantAbortDialog
import com.luczka.mycoffee.ui.screens.brewassistant.dialogs.BrewAssistantSaveDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrewAssistantMainScreen(
    uiState: BrewAssistantUiState,
    onAction: (BrewAssistantAction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = uiState.currentPage,
        pageCount = { uiState.pages.size }
    )

    val sheetState = rememberModalBottomSheetState()

    BackHandler {
        val action = BrewAssistantAction.OnPrevious
        onAction(action)
    }

    LaunchedEffect(uiState.currentPage) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(
                page = uiState.currentPage,
                animationSpec = tween(500)
            )
        }
    }

    if (uiState.showAbortDialog) {
        BrewAssistantAbortDialog(
            onNegative = {
                val action = BrewAssistantAction.OnHideAbortDialog
                onAction(action)
            },
            onPositive = {
                val action = BrewAssistantAction.OnBack
                onAction(action)
            }
        )
    }

    if (uiState.showFinishDialog) {
        BrewAssistantSaveDialog(
            uiState = uiState,
            onNegative = {
                val action = BrewAssistantAction.OnHideFinishDialog
                onAction(action)
            },
            onPositive = {
                val action = BrewAssistantAction.OnFinishBrewClicked
                onAction(action)
            }
        )
    }

    Scaffold(
        topBar = {
            BrewAssistantTopBar(onAction = onAction)
        },
        bottomBar = {
            AssistantBottomBar(
                uiState = uiState,
                onAction = onAction
            )
        }
    ) { innerPadding ->
        if (uiState.showBottomSheet) {
            val bottomSystemBarHeight = with(LocalDensity.current) {
                WindowInsets.systemBars.getBottom(this).toDp()
            }

            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    val action = BrewAssistantAction.OnHideBottomSheet
                    onAction(action)
                },
                tonalElevation = 0.dp
            ) {
                LazyColumn(contentPadding = PaddingValues(bottom = bottomSystemBarHeight)) {
                    uiState.recipeCategories.forEachIndexed { index, categoryUiState ->
                        item {
                            val paddingTop = if (index == 0) 0.dp else 24.dp
                            Text(
                                modifier = Modifier.padding(
                                    top = paddingTop,
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 8.dp
                                ),
                                text = stringResource(categoryUiState.nameRes),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        items(categoryUiState.recipes) {
                            val supportingText = buildSpannedString {
                                append("g of coffee")
                                append(" • ")
                                append("${it.coffeeRatio}:${it.waterRatio} ratio")
                                append(" • ")
                                append("${it.waterAmount} of water")
                            }.toString()

                            ListItem(
                                modifier = Modifier.clickable {

                                },
                                headlineContent = {
                                    Text(text = stringResource(it.nameRes))
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
        BrewAssistantContent(
            innerPadding = innerPadding,
            pagerState = pagerState,
            uiState = uiState,
            onAction = onAction
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BrewAssistantTopBar(onAction: (BrewAssistantAction) -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = {
                    val action = BrewAssistantAction.OnAbort
                    onAction(action)
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
private fun BrewAssistantContent(
    innerPadding: PaddingValues,
    pagerState: PagerState,
    uiState: BrewAssistantUiState,
    onAction: (BrewAssistantAction) -> Unit
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
                BrewAssistantPage.SELECTION -> AssistantSelectionScreen(
                    uiState = uiState,
                    onAction = onAction
                )

                BrewAssistantPage.PARAMETERS -> BrewAssistantParametersScreen(
                    uiState = uiState,
                    onAction = onAction
                )

                BrewAssistantPage.SUMMARY -> BrewAssistantSummaryScreen(
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
    uiState: BrewAssistantUiState,
    onAction: (BrewAssistantAction) -> Unit
) {
    Row(
        modifier = Modifier
            .heightIn(min = 80.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            enabled = !uiState.isFirstPage,
            onClick = {
                val action = BrewAssistantAction.OnPrevious
                onAction(action)
            }
        ) {
            Text(
                text = stringResource(id = R.string.action_previous),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onClick = {
                val action = BrewAssistantAction.OnNext
                onAction(action)
            }
        ) {
            Text(
                text = if (uiState.isLastPage) {
                    stringResource(id = R.string.action_save)
                } else {
                    stringResource(id = R.string.action_next)
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
