package com.luczka.mycoffee.ui.screens.brewassistant

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.CloseIconButton
import com.luczka.mycoffee.ui.components.TopAppBarTitle
import com.luczka.mycoffee.ui.model.CoffeeUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AssistantMainScreen(
    uiState: BrewAssistantUiState,
    navigateUp: () -> Unit,
    onCoffeeSelected: (CoffeeUiState) -> Unit,
    onUpdateAmountSelectionWholeNumber: (CoffeeUiState, Int) -> Unit,
    onUpdateAmountSelectionFractionalPart: (CoffeeUiState, Int) -> Unit,
    onUpdateAmountSelectionText: (CoffeeUiState, String) -> Unit,
    onUpdateHasRatio: () -> Unit,
    onUpdateCoffeeRatio: (Int) -> Unit,
    onUpdateWaterRatio: (Int) -> Unit,
    onUpdateRatioText: (String, String) -> Unit,
    onFinishBrew: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState()

    var showAbortDialog by rememberSaveable { mutableStateOf(false) }
    var showFinishDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) navigateUp()
    }

    BackHandler {
        if (pagerState.currentPage == 0) {
            if (uiState.selectedCoffees.isEmpty()) {
                navigateUp()
            } else {
                showAbortDialog = true
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
            navigateUp = navigateUp,
            onHideDialog = { showAbortDialog = false }
        )
    }

    if (showFinishDialog) {
        ShowFinishDialog(
            selectedCoffees = uiState.selectedCoffees,
            onFinishBrew = onFinishBrew,
            onHideDialog = { showFinishDialog = false }
        )
    }

    Scaffold(
        topBar = {
            AssistantTopBar(
                uiState = uiState,
                navigateUp = navigateUp,
                onShowAbortDialog = { showAbortDialog = true }
            )
        }
    ) { innerPadding ->
        AssistantContent(
            innerPadding = innerPadding,
            pagerState = pagerState,
            coroutineScope = coroutineScope,
            uiState = uiState,
            onCoffeeSelected = onCoffeeSelected,
            onUpdateAmountSelectionWholeNumber = onUpdateAmountSelectionWholeNumber,
            onUpdateAmountSelectionFractionalPart = onUpdateAmountSelectionFractionalPart,
            onUpdateAmountSelectionText = onUpdateAmountSelectionText,
            onUpdateHasRatio = onUpdateHasRatio,
            onUpdateCoffeeRatio = onUpdateCoffeeRatio,
            onUpdateRatioText = onUpdateRatioText,
            onUpdateWaterRatio = onUpdateWaterRatio,
            onShowFinishDialog = { showFinishDialog = true }
        )
    }
}

@Composable
private fun AbortBrewDialog(
    navigateUp: () -> Unit,
    onHideDialog: () -> Unit
) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.dialog_title_abort)) },
        text = { Text(text = stringResource(id = R.string.dialog_text_abort)) },
        confirmButton = {
            TextButton(
                onClick = {
                    onHideDialog()
                    navigateUp()
                },
                content = { Text(text = stringResource(id = R.string.dialog_action_abort)) }
            )
        },
        dismissButton = {
            TextButton(
                onClick = onHideDialog,
                content = { Text(text = stringResource(id = R.string.dialog_action_cancel)) }
            )
        },
        onDismissRequest = onHideDialog
    )
}


@Composable
private fun ShowFinishDialog(
    selectedCoffees: Map<CoffeeUiState, AmountSelectionUiState>,
    onFinishBrew: () -> Unit,
    onHideDialog: () -> Unit
) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.dialog_title_finish)) },
        text = {
            Column {
                selectedCoffees.forEach { (selectedCoffee, amountSelectionUiState) ->
                    Text(
                        text = stringResource(
                            id = R.string.dialog_text_finish,
                            amountSelectionUiState.selectedAmount,
                            selectedCoffee.name,
                            selectedCoffee.brand,
                        )
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onHideDialog()
                    onFinishBrew()
                },
                content = { Text(text = stringResource(id = R.string.dialog_action_finish)) }
            )
        },
        dismissButton = {
            TextButton(
                onClick = onHideDialog,
                content = { Text(text = stringResource(id = R.string.dialog_action_cancel)) }
            )
        },
        onDismissRequest = onHideDialog
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AssistantTopBar(
    uiState: BrewAssistantUiState,
    navigateUp: () -> Unit,
    onShowAbortDialog: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            CloseIconButton(
                onClick = {
                    if (uiState.selectedCoffees.isEmpty()) {
                        navigateUp()
                    } else {
                        onShowAbortDialog()
                    }
                }
            )
        },
        title = { TopAppBarTitle(text = stringResource(id = R.string.assistant_top_bar_title)) }
    )
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun AssistantContent(
    innerPadding: PaddingValues,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    uiState: BrewAssistantUiState,
    onCoffeeSelected: (CoffeeUiState) -> Unit,
    onUpdateAmountSelectionWholeNumber: (CoffeeUiState, Int) -> Unit,
    onUpdateAmountSelectionFractionalPart: (CoffeeUiState, Int) -> Unit,
    onUpdateAmountSelectionText: (CoffeeUiState, String) -> Unit,
    onUpdateHasRatio: () -> Unit,
    onUpdateCoffeeRatio: (Int) -> Unit,
    onUpdateRatioText: (String, String) -> Unit,
    onUpdateWaterRatio: (Int) -> Unit,
    onShowFinishDialog: () -> Unit
) {
    Column(modifier = Modifier.padding(innerPadding)) {
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
                    onUpdateAmountSelectionWholeNumber = onUpdateAmountSelectionWholeNumber,
                    onUpdateAmountSelectionFractionalPart = onUpdateAmountSelectionFractionalPart,
                    onUpdateAmountSelectionText = onUpdateAmountSelectionText,
                    onUpdateHasRatio = onUpdateHasRatio,
                    onUpdateCoffeeRatio = onUpdateCoffeeRatio,
                    onUpdateRatioText = onUpdateRatioText,
                    onUpdateWaterRatio = onUpdateWaterRatio
                )

                2 -> AssistantSummaryScreen(
                    uiState = uiState,
                )

                else -> {}
            }
        }
        Row(
            modifier = Modifier.padding(16.dp),
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
                uiState = uiState,
                onShowFinishDialog = onShowFinishDialog
            )
        }
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
        },
        content = { Text(text = stringResource(id = R.string.assistant_button_previous)) }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NextOrFinishButton(
    modifier: Modifier,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    uiState: BrewAssistantUiState,
    onShowFinishDialog: () -> Unit
) {
    val isNotLastPage = pagerState.currentPage != 2
    val enabled = when (pagerState.currentPage) {
        0 -> uiState.selectedCoffees.isNotEmpty()
        1 -> uiState.hasRatioValue && uiState.hasAmountValue
        2 -> true
        else -> false
    }
    val text = if (isNotLastPage) {
        stringResource(id = R.string.assistant_button_next)
    } else {
        stringResource(id = R.string.assistant_button_finish)
    }
    Button(
        modifier = modifier,
        enabled = enabled,
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
        },
        content = { Text(text = text) }
    )
}