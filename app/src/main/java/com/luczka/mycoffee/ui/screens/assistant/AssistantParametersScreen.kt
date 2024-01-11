package com.luczka.mycoffee.ui.screens.assistant

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.BrewParametersListItem
import com.luczka.mycoffee.ui.components.DoubleVerticalPager
import com.luczka.mycoffee.ui.components.ExpandableBrewParametersListItem
import com.luczka.mycoffee.ui.model.CoffeeUiState

@Composable
fun AssistantParametersScreen(
    uiState: BrewAssistantUiState,
    onUpdateAmountSelectionIntegerPart: (Int) -> Unit,
    onUpdateAmountSelectionDecimalPart: (Int) -> Unit,
    onUpdateAmountSelectionText: (String) -> Unit,
    onUpdateCoffeeAmountSelectionIntegerPart: (CoffeeUiState, Int) -> Unit,
    onUpdateCoffeeAmountSelectionDecimalPart: (CoffeeUiState, Int) -> Unit,
    onUpdateCoffeeAmountSelectionText: (CoffeeUiState, String) -> Unit,
    onUpdateCoffeeRatio: (Int) -> Unit,
    onUpdateRatioText: (String, String) -> Unit,
    onUpdateWaterRatio: (Int) -> Unit
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
                RatioSelectionDialog(
                    ratioSelectionUiState = uiState.ratioSelectionUiState,
                    onNegative = {
                        openDialog = false
                    },
                    onPositive = { coffeeRatio, waterRatio ->
                        openDialog = false
                        onUpdateRatioText(coffeeRatio, waterRatio)
                    }
                )
            }
            RatioSelectionListItemWithPicker(
                ratioSelectionUiState = uiState.ratioSelectionUiState,
                openPicker = openPicker,
                onClick = { openPicker = !openPicker },
                onUpdateCoffeeRatio = onUpdateCoffeeRatio,
                onUpdateWaterRatio = onUpdateWaterRatio,
                onOpenDialog = { openDialog = true }
            )
        }
        when (uiState) {
            is BrewAssistantUiState.NoneSelected -> {
                item {
                    var openPicker by rememberSaveable { mutableStateOf(false) }
                    var openDialog by rememberSaveable { mutableStateOf(false) }

                    if (openDialog) {
                        AmountSelectionDialog(
                            amountSelectionUiState = uiState.amountSelectionUiState,
                            onNegative = {
                                openDialog = false
                            },
                            onPositive = { amountText ->
                                openDialog = false
                                onUpdateAmountSelectionText(amountText)
                            },
                        )
                    }

                    AmountSelectionListItemWithPicker(
                        amountSelectionUiState = uiState.amountSelectionUiState,
                        openPicker = openPicker,
                        onClick = { openPicker = !openPicker },
                        onUpdateAmountSelectionIntegerPart = onUpdateAmountSelectionIntegerPart,
                        onUpdateAmountSelectionDecimalPart = onUpdateAmountSelectionDecimalPart,
                        onOpenDialog = { openDialog = true }
                    )
                }
            }

            is BrewAssistantUiState.CoffeeSelected -> {
                uiState.selectedCoffees.entries.forEachIndexed { index, entry ->
                    item {
                        val selectedCoffee = entry.key
                        val amountSelectionUiState = entry.value

                        var openAmountPicker by rememberSaveable { mutableStateOf(false) }
                        var openAmountDialog by rememberSaveable { mutableStateOf(false) }

                        if (openAmountDialog) {
                            AmountSelectionDialog(
                                maxAmount = selectedCoffee.amount?.toFloatOrNull() ?: 0.0f,
                                amountSelectionUiState = amountSelectionUiState,
                                onNegative = {
                                    openAmountDialog = false
                                },
                                onPositive = { selectedAmount ->
                                    openAmountDialog = false
                                    onUpdateCoffeeAmountSelectionText(
                                        selectedCoffee,
                                        selectedAmount
                                    )
                                }
                            )
                        }

                        AmountSelectionListItemWithPicker(
                            index = index + 1,
                            selectedCoffee = selectedCoffee,
                            amountSelectionUiState = amountSelectionUiState,
                            openAmountPicker = openAmountPicker,
                            onClick = { openAmountPicker = !openAmountPicker },
                            onUpdateCoffeeAmountSelectionIntegerPart = onUpdateCoffeeAmountSelectionIntegerPart,
                            onUpdateCoffeeAmountSelectionDecimalPart = onUpdateCoffeeAmountSelectionDecimalPart,
                            onOpenDialog = { openAmountDialog = true }
                        )
                    }
                }
            }
        }
        item {
            val index = when (uiState) {
                is BrewAssistantUiState.NoneSelected -> 2
                is BrewAssistantUiState.CoffeeSelected -> uiState.selectedCoffees.size + 1
            }

            BrewParametersListItem(
                index = index,
                overlineText = stringResource(id = R.string.assistant_water),
                headlineText = stringResource(
                    id = R.string.coffee_parameters_amount_with_unit,
                    uiState.waterAmount
                )
            )
        }
    }
}

@Composable
private fun RatioSelectionListItemWithPicker(
    ratioSelectionUiState: RatioSelectionUiState,
    openPicker: Boolean,
    onClick: () -> Unit,
    onUpdateCoffeeRatio: (Int) -> Unit,
    onUpdateWaterRatio: (Int) -> Unit,
    onOpenDialog: () -> Unit
) {
    ExpandableBrewParametersListItem(
        onClick = onClick,
        index = 0,
        overlineText = stringResource(id = R.string.assistant_ratio),
        headlineText = stringResource(
            id = R.string.assistant_ratio_format,
            ratioSelectionUiState.selectedCoffeeRatio,
            ratioSelectionUiState.selectedWaterRatio
        ),
        expanded = openPicker
    ) {
        DoubleVerticalPager(
            leftPagerPage = ratioSelectionUiState.coffeeRatioIndex,
            rightPagerPage = ratioSelectionUiState.waterRatioIndex,
            separator = ":",
            onUpdateLeftPager = onUpdateCoffeeRatio,
            onUpdateRightPager = onUpdateWaterRatio,
            leftPagerItems = ratioSelectionUiState.coffeeRatios,
            rightPagerItems = ratioSelectionUiState.waterRatios,
            onShowInputDialog = onOpenDialog
        )
    }
}

@Composable
private fun AmountSelectionListItemWithPicker(
    amountSelectionUiState: AmountSelectionUiState,
    openPicker: Boolean,
    onClick: () -> Unit,
    onUpdateAmountSelectionIntegerPart: (Int) -> Unit,
    onUpdateAmountSelectionDecimalPart: (Int) -> Unit,
    onOpenDialog: () -> Unit
) {
    ExpandableBrewParametersListItem(
        onClick = onClick,
        index = 1,
        overlineText = stringResource(id = R.string.assistant_coffee),
        headlineText = stringResource(
            id = R.string.coffee_parameters_amount_with_unit,
            amountSelectionUiState.selectedAmount
        ),
        expanded = openPicker
    ) {
        DoubleVerticalPager(
            leftPagerPage = amountSelectionUiState.integerPartIndex,
            rightPagerPage = amountSelectionUiState.decimalPartIndex,
            separator = ".",
            onUpdateLeftPager = { integerPartIndex ->
                onUpdateAmountSelectionIntegerPart(integerPartIndex)
            },
            onUpdateRightPager = { decimalPartIndex ->
                onUpdateAmountSelectionDecimalPart(decimalPartIndex)
            },
            leftPagerItems = amountSelectionUiState.integerParts,
            rightPagerItems = amountSelectionUiState.decimalParts,
            onShowInputDialog = onOpenDialog
        )
    }
}

@Composable
private fun AmountSelectionListItemWithPicker(
    index: Int,
    selectedCoffee: CoffeeUiState,
    amountSelectionUiState: AmountSelectionUiState,
    openAmountPicker: Boolean,
    onClick: () -> Unit,
    onUpdateCoffeeAmountSelectionIntegerPart: (CoffeeUiState, Int) -> Unit,
    onUpdateCoffeeAmountSelectionDecimalPart: (CoffeeUiState, Int) -> Unit,
    onOpenDialog: () -> Unit
) {
    ExpandableBrewParametersListItem(
        onClick = onClick,
        index = index,
        overlineText = stringResource(
            id = R.string.coffee_parameters_name_and_brand,
            selectedCoffee.name,
            selectedCoffee.brand
        ),
        headlineText = stringResource(
            id = R.string.coffee_parameters_amount_with_unit,
            amountSelectionUiState.selectedAmount
        ),
        expanded = openAmountPicker
    ) {
        DoubleVerticalPager(
            leftPagerPage = amountSelectionUiState.integerPartIndex,
            rightPagerPage = amountSelectionUiState.decimalPartIndex,
            separator = ".",
            onUpdateLeftPager = { integerPartIndex ->
                onUpdateCoffeeAmountSelectionIntegerPart(selectedCoffee, integerPartIndex)
            },
            onUpdateRightPager = { decimalPartIndex ->
                onUpdateCoffeeAmountSelectionDecimalPart(selectedCoffee, decimalPartIndex)
            },
            leftPagerItems = amountSelectionUiState.integerParts,
            rightPagerItems = amountSelectionUiState.decimalParts,
            onShowInputDialog = onOpenDialog
        )
    }
}

@Preview
@Composable
fun AssistantParametersScreenPreview() {
    val firstSelectedCoffee = CoffeeUiState(
        name = "ethiopia sami",
        brand = "monko.",
        amount = "250.0"
    )
    val uiState = BrewAssistantUiState.CoffeeSelected(
        selectedCoffees = mapOf(
            Pair(firstSelectedCoffee, AmountSelectionUiState(selectedAmount = "1.2"))
        )
    )
    AssistantParametersScreen(
        uiState = uiState,
        onUpdateAmountSelectionIntegerPart = { _ -> },
        onUpdateAmountSelectionDecimalPart = { _ -> },
        onUpdateAmountSelectionText = { _ -> },
        onUpdateCoffeeAmountSelectionIntegerPart = { _, _ -> },
        onUpdateCoffeeAmountSelectionDecimalPart = { _, _ -> },
        onUpdateCoffeeAmountSelectionText = { _, _ -> },
        onUpdateCoffeeRatio = {},
        onUpdateRatioText = { _, _ -> }
    ) {}
}

@Preview
@Composable
fun AssistantParametersScreenPreview1() {
    val firstSelectedCoffee = CoffeeUiState(
        name = "ethiopia sami",
        brand = "monko.",
        amount = "250.0"
    )
    val secondSelectedCoffee = CoffeeUiState(
        name = "Kolumbia",
        brand = "Mała Czarna",
        amount = "200.0"
    )
    val uiState = BrewAssistantUiState.CoffeeSelected(
        selectedCoffees = mapOf(
            Pair(firstSelectedCoffee, AmountSelectionUiState()),
            Pair(secondSelectedCoffee, AmountSelectionUiState(selectedAmount = "1.2"))
        )
    )
    AssistantParametersScreen(
        uiState = uiState,
        onUpdateAmountSelectionIntegerPart = { _ -> },
        onUpdateAmountSelectionDecimalPart = { _ -> },
        onUpdateAmountSelectionText = { _ -> },
        onUpdateCoffeeAmountSelectionIntegerPart = { _, _ -> },
        onUpdateCoffeeAmountSelectionDecimalPart = { _, _ -> },
        onUpdateCoffeeAmountSelectionText = { _, _ -> },
        onUpdateCoffeeRatio = {},
        onUpdateRatioText = { _, _ -> }
    ) {}
}