package com.luczka.mycoffee.ui.screen.assistant

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.luczka.mycoffee.ui.component.button.CloseIconButton
import com.luczka.mycoffee.ui.model.CoffeeUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AssistantMainScreen(
    uiState: BrewAssistantUiState,
    navigateUp: () -> Unit,
    onCoffeeSelected: (CoffeeUiState) -> Unit,
    onUpdateAmountSelectionIntegerPart: (Int) -> Unit,
    onUpdateAmountSelectionDecimalPart: (Int) -> Unit,
    onUpdateAmountSelectionText: (String) -> Unit,
    onUpdateCoffeeAmountSelectionIntegerPart: (CoffeeUiState, Int) -> Unit,
    onUpdateCoffeeAmountSelectionDecimalPart: (CoffeeUiState, Int) -> Unit,
    onUpdateCoffeeAmountSelectionText: (CoffeeUiState, String) -> Unit,
    onUpdateCoffeeRatio: (Int) -> Unit,
    onUpdateWaterRatio: (Int) -> Unit,
    onUpdateRatioText: (String, String) -> Unit,
    onUpdateRating: (Int?) -> Unit,
    onUpdateNotes: (String) -> Unit,
    onFinishBrew: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState()

    var showAbortDialog by rememberSaveable { mutableStateOf(false) }
    var showFinishDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) {
            navigateUp()
        }
    }

    BackHandler {
        if (pagerState.currentPage == 0) {
            when (uiState) {
                is BrewAssistantUiState.NoneSelected -> {
                    navigateUp()
                }

                is BrewAssistantUiState.CoffeeSelected -> {
                    if (uiState.selectedCoffees.isEmpty()) {
                        navigateUp()
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
        AbortBrewDialog(
            onNegative = {
                showAbortDialog = false
            },
            onPositive = {
                showAbortDialog = false
                navigateUp()
            }
        )
    }

    if (showFinishDialog) {
        FinishDialog(
            uiState = uiState,
            onNegative = {
                showFinishDialog = false
            },
            onPositive = {
                showFinishDialog = false
                onFinishBrew()
            }
        )
    }

    Scaffold(
        topBar = {
            AssistantTopBar(
                uiState = uiState,
                pagerState = pagerState,
                navigateUp = navigateUp,
                onShowAbortDialog = { showAbortDialog = true }
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
            onCoffeeSelected = onCoffeeSelected,
            onUpdateAmountSelectionIntegerPart = onUpdateAmountSelectionIntegerPart,
            onUpdateAmountSelectionDecimalPart = onUpdateAmountSelectionDecimalPart,
            onUpdateAmountSelectionText = onUpdateAmountSelectionText,
            onUpdateCoffeeAmountSelectionIntegerPart = onUpdateCoffeeAmountSelectionIntegerPart,
            onUpdateCoffeeAmountSelectionDecimalPart = onUpdateCoffeeAmountSelectionDecimalPart,
            onUpdateCoffeeAmountSelectionText = onUpdateCoffeeAmountSelectionText,
            onUpdateCoffeeRatio = onUpdateCoffeeRatio,
            onUpdateWaterRatio = onUpdateWaterRatio,
            onUpdateRatioText = onUpdateRatioText,
            onUpdateRating = onUpdateRating,
            onUpdateNotes = onUpdateNotes
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun AssistantTopBar(
    uiState: BrewAssistantUiState,
    pagerState: PagerState,
    navigateUp: () -> Unit,
    onShowAbortDialog: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            CloseIconButton(
                onClick = {
                    when (uiState) {
                        is BrewAssistantUiState.NoneSelected -> {
                            if (pagerState.currentPage == 0) {
                                navigateUp()
                            } else {
                                onShowAbortDialog()
                            }
                        }

                        is BrewAssistantUiState.CoffeeSelected -> {
                            if (uiState.selectedCoffees.isEmpty()) {
                                navigateUp()
                            } else {
                                onShowAbortDialog()
                            }
                        }
                    }
                }
            )
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
@OptIn(ExperimentalFoundationApi::class)
private fun AssistantContent(
    innerPadding: PaddingValues,
    pagerState: PagerState,
    uiState: BrewAssistantUiState,
    onCoffeeSelected: (CoffeeUiState) -> Unit,
    onUpdateAmountSelectionIntegerPart: (Int) -> Unit,
    onUpdateAmountSelectionDecimalPart: (Int) -> Unit,
    onUpdateAmountSelectionText: (String) -> Unit,
    onUpdateCoffeeAmountSelectionIntegerPart: (CoffeeUiState, Int) -> Unit,
    onUpdateCoffeeAmountSelectionDecimalPart: (CoffeeUiState, Int) -> Unit,
    onUpdateCoffeeAmountSelectionText: (CoffeeUiState, String) -> Unit,
    onUpdateCoffeeRatio: (Int) -> Unit,
    onUpdateRatioText: (String, String) -> Unit,
    onUpdateWaterRatio: (Int) -> Unit,
    onUpdateRating: (Int?) -> Unit,
    onUpdateNotes: (String) -> Unit,
) {
    Column(modifier = Modifier.padding(innerPadding)) {
        Divider(modifier = Modifier.fillMaxWidth())
        HorizontalPager(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            pageCount = 3,
            state = pagerState,
            beyondBoundsPageCount = 2,
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> AssistantSelectionScreen(
                    uiState = uiState,
                    onCoffeeSelected = onCoffeeSelected
                )

                1 -> AssistantParametersScreen(
                    uiState = uiState,
                    onUpdateAmountSelectionIntegerPart = onUpdateAmountSelectionIntegerPart,
                    onUpdateAmountSelectionDecimalPart = onUpdateAmountSelectionDecimalPart,
                    onUpdateAmountSelectionText = onUpdateAmountSelectionText,
                    onUpdateCoffeeAmountSelectionIntegerPart = onUpdateCoffeeAmountSelectionIntegerPart,
                    onUpdateCoffeeAmountSelectionDecimalPart = onUpdateCoffeeAmountSelectionDecimalPart,
                    onUpdateCoffeeAmountSelectionText = onUpdateCoffeeAmountSelectionText,
                    onUpdateCoffeeRatio = onUpdateCoffeeRatio,
                    onUpdateWaterRatio = onUpdateWaterRatio,
                    onUpdateRatioText = onUpdateRatioText
                )

                2 -> AssistantSummaryScreen(
                    uiState = uiState,
                    onUpdateRating = onUpdateRating,
                    onUpdateNotes = onUpdateNotes
                )

                else -> {}
            }
        }
        Divider(modifier = Modifier.fillMaxWidth())
    }
}

@OptIn(ExperimentalFoundationApi::class)
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

@OptIn(ExperimentalFoundationApi::class)
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