package com.luczka.mycoffee.ui.screens.assistant.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.custom.DoubleVerticalPager
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.screens.assistant.AmountSelectionUiState
import com.luczka.mycoffee.ui.screens.assistant.AssistantAction
import com.luczka.mycoffee.ui.screens.assistant.AssistantUiState
import com.luczka.mycoffee.ui.screens.assistant.RatioSelectionUiState
import com.luczka.mycoffee.ui.screens.assistant.components.AssistantParametersExpandableListItem
import com.luczka.mycoffee.ui.screens.assistant.components.AssistantParametersListItem
import com.luczka.mycoffee.ui.screens.assistant.dialogs.AssistantAmountSelectionDialog
import com.luczka.mycoffee.ui.screens.assistant.dialogs.AssistantRatioSelectionDialog

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
                textAlign = TextAlign.Center,
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
                AssistantRatioSelectionDialog(
                    ratioSelectionUiState = uiState.ratioSelectionUiState,
                    onNegative = {
                        openDialog = false
                    },
                    onPositive = { coffeeRatio, waterRatio ->
                        openDialog = false
                        val action = AssistantAction.OnRatioValueChanged(
                            coffeeRatioValue = coffeeRatio,
                            waterRatioValue = waterRatio
                        )
                        onAction(action)
                    }
                )
            }

            RatioSelectionListItemWithPicker(
                ratioSelectionUiState = uiState.ratioSelectionUiState,
                openPicker = openPicker,
                onClick = { openPicker = !openPicker },
                onUpdateLeftPager = { index ->
                    val action = AssistantAction.OnCoffeeRatioIndexChanged(coffeeRatioIndex = index)
                    onAction(action)
                },
                onUpdateRightPager = { index ->
                    val action = AssistantAction.OnWaterRatioIndexChanged(waterRatioIndex = index)
                    onAction(action)
                },
                onOpenDialog = { openDialog = true }
            )
        }
        when (uiState) {
            is AssistantUiState.NoneSelected -> {
                item {
                    var openPicker by rememberSaveable { mutableStateOf(false) }
                    var openDialog by rememberSaveable { mutableStateOf(false) }

                    if (openDialog) {
                        AssistantAmountSelectionDialog(
                            amountSelectionUiState = uiState.amountSelectionUiState,
                            onNegative = {
                                openDialog = false
                            },
                            onPositive = { amountText ->
                                openDialog = false
                                val action = AssistantAction.OnCoffeeAmountSelectionValueChanged(
                                    key = null,
                                    amountSelectionValue = amountText
                                )
                                onAction(action)
                            },
                        )
                    }

                    AmountSelectionListItemWithPicker(
                        amountSelectionUiState = uiState.amountSelectionUiState,
                        openPicker = openPicker,
                        onClick = { openPicker = !openPicker },
                        onUpdateLeftPager = { index ->
                            val action = AssistantAction.OnCoffeeAmountSelectionIntegerPartIndexChanged(
                                key = null,
                                integerPartIndex = index
                            )
                            onAction(action)
                        },
                        onUpdateRightPager = { index ->
                            val action = AssistantAction.OnCoffeeAmountSelectionDecimalPartIndexChanged(
                                key = null,
                                decimalPartIndex = index
                            )
                            onAction(action)
                        },
                        onOpenDialog = { openDialog = true }
                    )
                }
            }

            is AssistantUiState.CoffeeSelected -> {
                uiState.selectedCoffees.entries.forEachIndexed { index, entry ->
                    item {
                        val selectedCoffee = entry.key
                        val amountSelectionUiState = entry.value

                        var openAmountPicker by rememberSaveable { mutableStateOf(false) }
                        var openAmountDialog by rememberSaveable { mutableStateOf(false) }

                        if (openAmountDialog) {
                            AssistantAmountSelectionDialog(
                                maxAmount = selectedCoffee.amount?.toFloatOrNull() ?: 0.0f,
                                amountSelectionUiState = amountSelectionUiState,
                                onNegative = {
                                    openAmountDialog = false
                                },
                                onPositive = { selectedAmount ->
                                    openAmountDialog = false
                                    val action = AssistantAction.OnCoffeeAmountSelectionValueChanged(
                                        key = selectedCoffee,
                                        amountSelectionValue = selectedAmount
                                    )
                                    onAction(action)
                                }
                            )
                        }

                        AmountSelectionListItemWithPicker(
                            index = index + 1,
                            selectedCoffee = selectedCoffee,
                            amountSelectionUiState = amountSelectionUiState,
                            openAmountPicker = openAmountPicker,
                            onClick = { openAmountPicker = !openAmountPicker },
                            onUpdateCoffeeAmountSelectionIntegerPart = { key, index ->
                                val action = AssistantAction.OnCoffeeAmountSelectionIntegerPartIndexChanged(
                                    key = key,
                                    integerPartIndex = index
                                )
                                onAction(action)
                            },
                            onUpdateCoffeeAmountSelectionDecimalPart = { key, index ->
                                val action = AssistantAction.OnCoffeeAmountSelectionDecimalPartIndexChanged(
                                    key = key,
                                    decimalPartIndex = index
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
                is AssistantUiState.NoneSelected -> 2
                is AssistantUiState.CoffeeSelected -> uiState.selectedCoffees.size + 1
            }
            AssistantParametersListItem(
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
    onUpdateLeftPager: (Int) -> Unit,
    onUpdateRightPager: (Int) -> Unit,
    onOpenDialog: () -> Unit
) {
    AssistantParametersExpandableListItem(
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
            onLeftPagerIndexChanged = onUpdateLeftPager,
            onRightPagerIndexChanged = onUpdateRightPager,
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
    onUpdateLeftPager: (Int) -> Unit,
    onUpdateRightPager: (Int) -> Unit,
    onOpenDialog: () -> Unit
) {
    AssistantParametersExpandableListItem(
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
            onLeftPagerIndexChanged = onUpdateLeftPager,
            onRightPagerIndexChanged = onUpdateRightPager,
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
    AssistantParametersExpandableListItem(
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
            onLeftPagerIndexChanged = { integerPartIndex ->
                onUpdateCoffeeAmountSelectionIntegerPart(selectedCoffee, integerPartIndex)
            },
            onRightPagerIndexChanged = { decimalPartIndex ->
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
    val uiState = AssistantUiState.CoffeeSelected(
        selectedCoffees = mapOf(
            Pair(firstSelectedCoffee, AmountSelectionUiState(selectedAmount = "1.2"))
        )
    )
    AssistantParametersScreen(
        uiState = uiState,
        onAction = {}
    )
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
    val uiState = AssistantUiState.CoffeeSelected(
        selectedCoffees = mapOf(
            Pair(firstSelectedCoffee, AmountSelectionUiState()),
            Pair(secondSelectedCoffee, AmountSelectionUiState(selectedAmount = "1.2"))
        )
    )
    AssistantParametersScreen(
        uiState = uiState,
        onAction = {}
    )
}