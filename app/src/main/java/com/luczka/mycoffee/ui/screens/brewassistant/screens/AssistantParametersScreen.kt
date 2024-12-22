package com.luczka.mycoffee.ui.screens.brewassistant.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.icons.KeyboardIcon
import com.luczka.mycoffee.ui.components.icons.PauseCircleIcon
import com.luczka.mycoffee.ui.components.icons.PlayCircleIcon
import com.luczka.mycoffee.ui.components.icons.StopCircleIcon
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.screens.brewassistant.AssistantAction
import com.luczka.mycoffee.ui.screens.brewassistant.AssistantUiState
import com.luczka.mycoffee.ui.screens.brewassistant.components.AssistantParametersExpandableListItem
import com.luczka.mycoffee.ui.screens.brewassistant.components.AssistantParametersListItem
import com.luczka.mycoffee.ui.screens.brewassistant.components.DoubleVerticalPager
import com.luczka.mycoffee.ui.screens.brewassistant.components.DoubleVerticalPagerState
import com.luczka.mycoffee.ui.screens.brewassistant.dialogs.DoubleVerticalPagerInputDialog

@Composable
fun AssistantParametersScreen(
    uiState: AssistantUiState,
    onAction: (AssistantAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.assistant_parameters_screen_title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        item {
            Text(
                modifier = Modifier.padding(
                    top = 24.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp
                ),
                text = "Recipe (optional)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        item {
            ListItem(
                modifier = Modifier.clickable {
                    val action = AssistantAction.OnSelectRecipeClicked
                    onAction(action)
                },
                headlineContent = {
                    Text("Select recipe")
                }
            )
        }
        item {
            Text(
                modifier = Modifier.padding(
                    top = 24.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp
                ),
                text = "Brew parameters",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        item {
            var openPicker by rememberSaveable { mutableStateOf(true) }
            var openDialog by rememberSaveable { mutableStateOf(false) }

            if (openDialog) {
                DoubleVerticalPagerInputDialog(
                    doubleVerticalPagerState = uiState.ratioSelectionUiState,
                    titleText = stringResource(R.string.dialog_double_vertical_pager_input_title_ratio),
                    onNegative = {
                        openDialog = false
                    },
                    onPositive = { coffeeRatio, waterRatio ->
                        openDialog = false
                        val action = AssistantAction.OnRatioSelectionCoffeeAndWaterValueChanged(
                            leftInputValue = coffeeRatio,
                            rightInputValue = waterRatio
                        )
                        onAction(action)
                    }
                )
            }

            RatioSelectionListItemWithPicker(
                ratioDoubleVerticalPagerState = uiState.ratioSelectionUiState,
                openPicker = openPicker,
                onClick = { openPicker = !openPicker },
                onUpdateLeftPager = { index ->
                    val action = AssistantAction.OnRatioSelectionCoffeeIndexChanged(leftPagerPageIndex = index)
                    onAction(action)
                },
                onUpdateRightPager = { index ->
                    val action = AssistantAction.OnRatioSelectionWaterIndexChanged(rightPagerPageIndex = index)
                    onAction(action)
                },
                onOpenDialog = { openDialog = true }
            )
        }
        when (uiState) {
            is AssistantUiState.NoneSelected -> {
                item {
                    var openAmountPicker by rememberSaveable { mutableStateOf(false) }
                    var openAmountDialog by rememberSaveable { mutableStateOf(false) }

                    if (openAmountDialog) {
                        DoubleVerticalPagerInputDialog(
                            doubleVerticalPagerState = uiState.defaultAmountDoubleVerticalPagerState,
                            titleText = stringResource(R.string.dialog_title_select_amount),
                            onNegative = {
                                openAmountDialog = false
                            },
                            onPositive = { leftInputValue, rightInputValue ->
                                openAmountDialog = false
                                val action = AssistantAction.OnAmountSelectionIntegerAndFractionalPartsValueChanged(
                                    key = null,
                                    leftInputValue = leftInputValue,
                                    rightInputValue = rightInputValue
                                )
                                onAction(action)
                            },
                        )
                    }

                    AmountSelectionListItemWithPicker(
                        amountDoubleVerticalPagerState = uiState.defaultAmountDoubleVerticalPagerState,
                        openPicker = openAmountPicker,
                        onClick = { openAmountPicker = !openAmountPicker },
                        onUpdateLeftPager = { index ->
                            val action = AssistantAction.OnAmountSelectionIntegerPartIndexChanged(
                                key = null,
                                leftPagerPageIndex = index
                            )
                            onAction(action)
                        },
                        onUpdateRightPager = { index ->
                            val action = AssistantAction.OnAmountSelectionFractionalPartIndexChanged(
                                key = null,
                                rightPagerPageIndex = index
                            )
                            onAction(action)
                        },
                        onOpenDialog = { openAmountDialog = true }
                    )
                }
            }

            is AssistantUiState.CoffeeSelected -> {
                uiState.selectedCoffees.entries.forEachIndexed { index, entry ->
                    item {
                        val selectedCoffee = entry.key
                        val amountDoubleVerticalPagerState = entry.value

                        var openAmountPicker by rememberSaveable { mutableStateOf(false) }
                        var openAmountDialog by rememberSaveable { mutableStateOf(false) }

                        if (openAmountDialog) {
                            DoubleVerticalPagerInputDialog(
                                doubleVerticalPagerState = amountDoubleVerticalPagerState,
                                titleText = stringResource(R.string.dialog_title_select_amount),
                                onNegative = {
                                    openAmountDialog = false
                                },
                                onPositive = { leftInputValue, rightInputValue ->
                                    openAmountDialog = false
                                    val action = AssistantAction.OnAmountSelectionIntegerAndFractionalPartsValueChanged(
                                        key = selectedCoffee,
                                        leftInputValue = leftInputValue,
                                        rightInputValue = rightInputValue
                                    )
                                    onAction(action)

                                },
                            )
                        }

                        AmountSelectionListItemWithPicker(
                            index = index + 1,
                            selectedCoffee = selectedCoffee,
                            amountDoubleVerticalPagerState = amountDoubleVerticalPagerState,
                            openAmountPicker = openAmountPicker,
                            onClick = { openAmountPicker = !openAmountPicker },
                            onUpdateCoffeeAmountSelectionIntegerPart = { key, index ->
                                val action = AssistantAction.OnAmountSelectionIntegerPartIndexChanged(
                                    key = key,
                                    leftPagerPageIndex = index
                                )
                                onAction(action)
                            },
                            onUpdateCoffeeAmountSelectionDecimalPart = { key, index ->
                                val action = AssistantAction.OnAmountSelectionFractionalPartIndexChanged(
                                    key = key,
                                    rightPagerPageIndex = index
                                )
                                onAction(action)
                            },
                            onOpenDialog = { openAmountDialog = true }
                        )
                    }
                }
            }
        }
        item {
            val index = when (uiState) {
                is AssistantUiState.NoneSelected -> 3
                is AssistantUiState.CoffeeSelected -> uiState.selectedCoffees.size + 2
            }

            AssistantParametersListItem(
                index = index,
                overlineText = stringResource(id = R.string.assistant_water),
                headlineText = stringResource(
                    id = R.string.format_coffee_amount_grams,
                    uiState.waterAmount
                )
            )
        }
        item {
            Text(
                modifier = Modifier.padding(
                    top = 24.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp
                ),
                text = "Brewing time (optional)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        item {
            var openTimerDialog by rememberSaveable { mutableStateOf(false) }

            val index = when (uiState) {
                is AssistantUiState.NoneSelected -> 4
                is AssistantUiState.CoffeeSelected -> uiState.selectedCoffees.size + 3
            }

            AssistantParametersListItem(
                index = index,
                overlineText = "Time",
                headlineText = uiState.formattedTime,
                trailingContent = {
                    Row(modifier = Modifier.offset(x = 12.dp)) {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            KeyboardIcon()
                        }
                        IconButton(
                            onClick = {
                                val action = AssistantAction.OnStartStopTimerClicked
                                onAction(action)
                            }
                        ) {
                            if(uiState.isTimerRunning) {
                                PauseCircleIcon()
                            } else {
                                PlayCircleIcon()
                            }
                        }
                        IconButton(
                            onClick = {
                                val action = AssistantAction.OnResetTimerClicked
                                onAction(action)
                            }
                        ) {
                            StopCircleIcon()
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun RatioSelectionListItemWithPicker(
    ratioDoubleVerticalPagerState: DoubleVerticalPagerState,
    openPicker: Boolean,
    onClick: () -> Unit,
    onUpdateLeftPager: (Int) -> Unit,
    onUpdateRightPager: (Int) -> Unit,
    onOpenDialog: () -> Unit
) {
    AssistantParametersExpandableListItem(
        onClick = onClick,
        index = 1,
        overlineText = stringResource(id = R.string.assistant_ratio),
        headlineText = stringResource(
            id = R.string.format_ratio,
            ratioDoubleVerticalPagerState.currentLeftPagerItem(),
            ratioDoubleVerticalPagerState.currentRightPagerItem(),
        ),
        expanded = openPicker
    ) {
        DoubleVerticalPager(
            doubleVerticalPagerState = ratioDoubleVerticalPagerState,
            onLeftPagerIndexChanged = onUpdateLeftPager,
            onRightPagerIndexChanged = onUpdateRightPager,
            onShowInputDialog = onOpenDialog
        )
    }
}

@Composable
private fun AmountSelectionListItemWithPicker(
    amountDoubleVerticalPagerState: DoubleVerticalPagerState,
    openPicker: Boolean,
    onClick: () -> Unit,
    onUpdateLeftPager: (Int) -> Unit,
    onUpdateRightPager: (Int) -> Unit,
    onOpenDialog: () -> Unit
) {
    val integerPart = amountDoubleVerticalPagerState.currentLeftPagerItem()
    val fractionalPart = amountDoubleVerticalPagerState.currentRightPagerItem()
    AssistantParametersExpandableListItem(
        onClick = onClick,
        index = 2,
        overlineText = stringResource(id = R.string.assistant_coffee),
        headlineText = stringResource(
            id = R.string.format_coffee_integer_part_decimal_part_grams,
            integerPart,
            fractionalPart
        ),
        expanded = openPicker
    ) {
        DoubleVerticalPager(
            doubleVerticalPagerState = amountDoubleVerticalPagerState,
            onLeftPagerIndexChanged = onUpdateLeftPager,
            onRightPagerIndexChanged = onUpdateRightPager,
            onShowInputDialog = onOpenDialog
        )
    }
}

@Composable
private fun AmountSelectionListItemWithPicker(
    index: Int,
    selectedCoffee: CoffeeUiState,
    amountDoubleVerticalPagerState: DoubleVerticalPagerState,
    openAmountPicker: Boolean,
    onClick: () -> Unit,
    onUpdateCoffeeAmountSelectionIntegerPart: (CoffeeUiState, Int) -> Unit,
    onUpdateCoffeeAmountSelectionDecimalPart: (CoffeeUiState, Int) -> Unit,
    onOpenDialog: (() -> Unit)?
) {
    val integerPart = amountDoubleVerticalPagerState.currentLeftPagerItem()
    val fractionalPart = amountDoubleVerticalPagerState.currentRightPagerItem()
    AssistantParametersExpandableListItem(
        onClick = onClick,
        index = index,
        overlineText = stringResource(
            id = R.string.format_coffee_name_coma_brand,
            selectedCoffee.originOrName,
            selectedCoffee.roasterOrBrand
        ),
        headlineText = stringResource(
            id = R.string.format_coffee_integer_part_decimal_part_grams,
            integerPart,
            fractionalPart
        ),
        expanded = openAmountPicker
    ) {
        DoubleVerticalPager(
            doubleVerticalPagerState = amountDoubleVerticalPagerState,
            onLeftPagerIndexChanged = { integerPartIndex ->
                onUpdateCoffeeAmountSelectionIntegerPart(selectedCoffee, integerPartIndex)
            },
            onRightPagerIndexChanged = { decimalPartIndex ->
                onUpdateCoffeeAmountSelectionDecimalPart(selectedCoffee, decimalPartIndex)
            },
            onShowInputDialog = onOpenDialog
        )
    }
}

//@Preview
//@Composable
//fun AssistantParametersScreenPreview() {
//    val firstSelectedCoffee = CoffeeUiState(
//        name = "ethiopia sami",
//        brand = "monko.",
//        amount = "250.0"
//    )
//    val uiState = AssistantUiState.CoffeeSelected(
//        selectedCoffees = mapOf(
//            Pair(firstSelectedCoffee, AmountSelectionUiState(selectedAmount = "1.2"))
//        )
//    )
//    AssistantParametersScreen(
//        uiState = uiState,
//        onAction = {}
//    )
//}
//
//@Preview
//@Composable
//fun AssistantParametersScreenPreview1() {
//    val firstSelectedCoffee = CoffeeUiState(
//        name = "ethiopia sami",
//        brand = "monko.",
//        amount = "250.0"
//    )
//    val secondSelectedCoffee = CoffeeUiState(
//        name = "Kolumbia",
//        brand = "Mała Czarna",
//        amount = "200.0"
//    )
//    val uiState = AssistantUiState.CoffeeSelected(
//        selectedCoffees = mapOf(
//            Pair(firstSelectedCoffee, AmountSelectionUiState()),
//            Pair(secondSelectedCoffee, AmountSelectionUiState(selectedAmount = "1.2"))
//        )
//    )
//    AssistantParametersScreen(
//        uiState = uiState,
//        onAction = {}
//    )
//}