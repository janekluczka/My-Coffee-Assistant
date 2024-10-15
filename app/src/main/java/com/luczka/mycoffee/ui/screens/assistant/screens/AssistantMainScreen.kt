package com.luczka.mycoffee.ui.screens.assistant.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.icons.CloseIcon
import com.luczka.mycoffee.ui.screens.assistant.AssistantAction
import com.luczka.mycoffee.ui.screens.assistant.AssistantUiState
import com.luczka.mycoffee.ui.screens.assistant.dialogs.AssistantAbortDialog
import com.luczka.mycoffee.ui.screens.assistant.dialogs.AssistantFinishDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantMainScreen(
    uiState: AssistantUiState,
    onAction: (AssistantAction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })

    var showAbortDialog by rememberSaveable { mutableStateOf(false) }
    var showFinishDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) {
            onAction(AssistantAction.NavigateUp)
        }
    }

    BackHandler {
        if (pagerState.currentPage == 0) {
            when (uiState) {
                is AssistantUiState.NoneSelected -> {
                    onAction(AssistantAction.NavigateUp)
                }

                is AssistantUiState.CoffeeSelected -> {
                    if (uiState.selectedCoffees.isEmpty()) {
                        onAction(AssistantAction.NavigateUp)
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
                onAction(AssistantAction.NavigateUp)
            }
        )
    }

    if (showFinishDialog) {
        AssistantFinishDialog(
            uiState = uiState,
            onNegative = {
                showFinishDialog = false
            },
            onPositive = {
                showFinishDialog = false
                onAction(AssistantAction.OnFinishBrew)
            }
        )
    }

    Scaffold(
        topBar = {
            AssistantTopBar(
                uiState = uiState,
                pagerState = pagerState,
                navigateUp = {
                    onAction(AssistantAction.NavigateUp)
                },
                onShowAbortDialog = {
                    showAbortDialog = true
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .height(80.dp)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PreviousButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    pagerState = pagerState,
                    coroutineScope = coroutineScope
                )
                NextOrFinishButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    pagerState = pagerState,
                    coroutineScope = coroutineScope,
                    onShowFinishDialog = { showFinishDialog = true }
                )
            }
        }
    ) { innerPadding ->
        AssistantContent(
            innerPadding = innerPadding,
            pagerState = pagerState,
            uiState = uiState,
            onAction = onAction
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AssistantTopBar(
    uiState: AssistantUiState,
    pagerState: PagerState,
    navigateUp: () -> Unit,
    onShowAbortDialog: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = {
                    when (uiState) {
                        is AssistantUiState.NoneSelected -> {
                            if (pagerState.currentPage == 0) {
                                navigateUp()
                            } else {
                                onShowAbortDialog()
                            }
                        }

                        is AssistantUiState.CoffeeSelected -> {
                            if (uiState.selectedCoffees.isEmpty()) {
                                navigateUp()
                            } else {
                                onShowAbortDialog()
                            }
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
        ) { page ->
            when (page) {
                0 -> AssistantSelectionScreen(
                    uiState = uiState,
                    onAction = onAction
                )

                1 -> AssistantParametersScreen(
                    uiState = uiState,
                    onAction = onAction
                )

                2 -> AssistantSummaryScreen(
                    uiState = uiState,
                    onAction = onAction
                )

                else -> {}
            }
        }
        Divider(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun PreviousButton(
    modifier: Modifier,
    pagerState: PagerState,
    coroutineScope: CoroutineScope
) {
    Button(
        modifier = modifier,
        enabled = pagerState.currentPage != 0,
        onClick = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(
                    page = pagerState.currentPage - 1,
                    animationSpec = tween(500)
                )
            }
        }
    ) {
        Text(text = stringResource(id = R.string.assistant_button_previous))
    }
}

@Composable
private fun NextOrFinishButton(
    modifier: Modifier,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    onShowFinishDialog: () -> Unit
) {
    val isNotLastPage = pagerState.currentPage != 2
    val text = if (isNotLastPage) {
        stringResource(id = R.string.assistant_button_next)
    } else {
        stringResource(id = R.string.assistant_button_finish)
    }
    Button(
        modifier = modifier,
        onClick = {
            if (isNotLastPage) {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(
                        page = pagerState.currentPage + 1,
                        animationSpec = tween(500)
                    )
                }
            } else {
                onShowFinishDialog()
            }
        }
    ) {
        Text(text = text)
    }
}