package com.luczka.mycoffee.ui.screens.brewassistant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.FilteredOutlinedTextField
import com.luczka.mycoffee.ui.model.CoffeeUiState
import com.luczka.mycoffee.ui.screens.brewassistant.components.BrewParametersListItem
import com.luczka.mycoffee.ui.screens.brewassistant.components.DoubleVerticalPager
import com.luczka.mycoffee.ui.screens.brewassistant.components.ExpandableBrewParametersListItem
import com.luczka.mycoffee.ui.screens.brewassistant.components.ScreenTitle
import com.luczka.mycoffee.ui.screens.brewassistant.components.SectionSpacer
import com.luczka.mycoffee.ui.screens.brewassistant.components.SectionTitle
import com.luczka.mycoffee.ui.screens.brewassistant.components.SelectedCoffeeListItem
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme
import com.luczka.mycoffee.util.isPositiveFloat

@Composable
fun AssistantParametersScreen(
    uiState: BrewAssistantUiState,
    onUpdateAmountSelectionWholeNumber: (CoffeeUiState, Int) -> Unit,
    onUpdateAmountSelectionFractionalPart: (CoffeeUiState, Int) -> Unit,
    onUpdateAmountSelectionText: (CoffeeUiState, String) -> Unit,
    onUpdateHasRatio: () -> Unit,
    onUpdateCoffeeRatio: (Int) -> Unit,
    onUpdateRatioText: (String, String) -> Unit,
    onUpdateWaterRatio: (Int) -> Unit
) {
    if (uiState.selectedCoffees.isEmpty()) return

    val moreThanOneCoffeeSelected = uiState.selectedCoffees.size > 1

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            ScreenTitle(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.assistant_screen_2_title)
            )
        }
        item {
            SectionSpacer()
        }
        item {
            val title = if (moreThanOneCoffeeSelected) {
                stringResource(id = R.string.assistant_selected_coffees)
            } else {
                stringResource(id = R.string.assistant_selected_coffee)
            }
            SectionTitle(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = title
            )
        }
        uiState.selectedCoffees.keys.forEachIndexed { index, selectedCoffee ->
            item {
                SelectedCoffeeListItem(
                    index = index,
                    showCoffeeIndex = moreThanOneCoffeeSelected,
                    coffeeUiState = selectedCoffee
                )
            }
        }
        item {
            SectionSpacer()
        }
        item {
            SectionTitle(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Brew parameters"
            )
        }
        uiState.selectedCoffees.entries.forEachIndexed { index, entry ->
            item {
                val selectedCoffee = entry.key
                val amountSelectionUiState = entry.value

                var openAmountPicker by rememberSaveable { mutableStateOf(false) }
                var openAmountDialog by rememberSaveable { mutableStateOf(false) }

                if (openAmountDialog) {
                    AmountSelectionDialog(
                        selectedCoffee = selectedCoffee,
                        selectedAmount = amountSelectionUiState.selectedAmount,
                        onUpdateCoffeeAmountText = { selectedAmount ->
                            onUpdateAmountSelectionText(
                                selectedCoffee,
                                selectedAmount
                            )
                        },
                        onHideDialog = { openAmountDialog = false }
                    )
                }

                val showAmount = amountSelectionUiState.selectedAmount.isPositiveFloat()

                val overlineText = if (showAmount) {
                    if (moreThanOneCoffeeSelected) {
                        stringResource(
                            id = R.string.assistant_selected_amount_with_coffee_index,
                            index + 1
                        )
                    } else {
                        stringResource(id = R.string.assistant_selected_amount)
                    }
                } else {
                    if (moreThanOneCoffeeSelected) {
                        stringResource(
                            id = R.string.assistant_coffee_index,
                            index + 1
                        )
                    } else {
                        null
                    }
                }

                val headlineText = if (showAmount) {
                    stringResource(
                        id = R.string.coffee_parameters_amount_with_unit,
                        amountSelectionUiState.selectedAmount
                    )
                } else {
                    stringResource(id = R.string.assistant_select_amount)
                }

                AmountSelectionListItemWithPicker(
                    index = index,
                    overlineText = overlineText,
                    headlineText = headlineText,
                    selectedCoffee = selectedCoffee,
                    amountSelectionUiState = amountSelectionUiState,
                    openAmountPicker = openAmountPicker,
                    onClick = { openAmountPicker = !openAmountPicker },
                    onUpdateAmountSelectionWholeNumber = onUpdateAmountSelectionWholeNumber,
                    onUpdateAmountSelectionFractionalPart = onUpdateAmountSelectionFractionalPart,
                    onOpenDialog = { openAmountDialog = true }
                )
            }
        }
        item {
            var openRatioPicker by rememberSaveable { mutableStateOf(false) }
            var openRatioDialog by rememberSaveable { mutableStateOf(false) }

            if (openRatioDialog) {
                RatioSelectionDialog(
                    selectedCoffeeRatio = uiState.ratioSelectionUiState.selectedCoffeeRatio,
                    selectedWaterRatio = uiState.ratioSelectionUiState.selectedWaterRatio,
                    onUpdateRatioText = onUpdateRatioText,
                    onHideDialog = { openRatioDialog = false }
                )
            }

            val overlineText = if (uiState.hasRatioValue) {
                stringResource(id = R.string.assistant_selected_ratio)
            } else {
                null
            }

            val headlineText = if (uiState.hasRatioValue) {
                stringResource(
                    id = R.string.assistant_ratio_format,
                    uiState.ratioSelectionUiState.selectedCoffeeRatio,
                    uiState.ratioSelectionUiState.selectedWaterRatio
                )
            } else {
                stringResource(id = R.string.assistant_select_ratio)
            }

            RatioSelectionListItemWithPicker(
                index = uiState.selectedCoffees.size,
                uiState = uiState,
                overlineText = overlineText,
                headlineText = headlineText,
                openRatioPicker = openRatioPicker,
                onClick = {
                    openRatioPicker = !openRatioPicker
                    if (!uiState.hasRatioValue) onUpdateHasRatio()
                },
                onUpdateCoffeeRatio = onUpdateCoffeeRatio,
                onUpdateWaterRatio = onUpdateWaterRatio,
                onOpenDialog = { openRatioDialog = true }
            )
        }
        item {
            val showWaterAmount = uiState.hasRatioValue

            val overlineText = if (showWaterAmount) {
                stringResource(id = R.string.assistant_water_amount)
            } else {
                null
            }

            val headlineText = if (showWaterAmount) {
                stringResource(
                    id = R.string.coffee_parameters_amount_with_unit,
                    uiState.waterAmount
                )
            } else {
                stringResource(id = R.string.assistant_water_amount)
            }

            BrewParametersListItem(
                index = uiState.selectedCoffees.size + 1,
                overlineText = overlineText,
                headlineText = headlineText
            )
        }
    }
}

@Composable
private fun RatioSelectionListItemWithPicker(
    index: Int,
    openRatioPicker: Boolean,
    uiState: BrewAssistantUiState,
    overlineText: String?,
    headlineText: String,
    onClick: () -> Unit,
    onUpdateCoffeeRatio: (Int) -> Unit,
    onUpdateWaterRatio: (Int) -> Unit,
    onOpenDialog: () -> Unit
) {
    ExpandableBrewParametersListItem(
        onClick = onClick,
        index = index,
        overlineText = overlineText,
        headlineText = headlineText,
        expanded = openRatioPicker
    ) {
        DoubleVerticalPager(
            leftPagerInitialPage = uiState.ratioSelectionUiState.coffeeRatioIndex,
            rightPagerInitialPage = uiState.ratioSelectionUiState.waterRatioIndex,
            separator = ":",
            onUpdateLeftPager = onUpdateCoffeeRatio,
            onUpdateRightPager = onUpdateWaterRatio,
            leftPagerItems = uiState.ratioSelectionUiState.coffeeRatios,
            rightPagerItems = uiState.ratioSelectionUiState.waterRatios,
            onShowInputDialog = onOpenDialog
        )
    }
}

@Composable
private fun AmountSelectionListItemWithPicker(
    index: Int,
    overlineText: String?,
    headlineText: String,
    selectedCoffee: CoffeeUiState,
    amountSelectionUiState: AmountSelectionUiState,
    openAmountPicker: Boolean,
    onClick: () -> Unit,
    onUpdateAmountSelectionWholeNumber: (CoffeeUiState, Int) -> Unit,
    onUpdateAmountSelectionFractionalPart: (CoffeeUiState, Int) -> Unit,
    onOpenDialog: () -> Unit
) {
    ExpandableBrewParametersListItem(
        onClick = onClick,
        index = index,
        overlineText = overlineText,
        headlineText = headlineText,
        expanded = openAmountPicker
    ) {
        DoubleVerticalPager(
            leftPagerInitialPage = amountSelectionUiState.wholeNumberIndex,
            rightPagerInitialPage = amountSelectionUiState.fractionalPartIndex,
            separator = ".",
            onUpdateLeftPager = { wholeNumberIndex ->
                onUpdateAmountSelectionWholeNumber(
                    selectedCoffee,
                    wholeNumberIndex
                )
            },
            onUpdateRightPager = { fractionalPartIndex ->
                onUpdateAmountSelectionFractionalPart(
                    selectedCoffee,
                    fractionalPartIndex
                )
            },
            leftPagerItems = amountSelectionUiState.wholeNumbers,
            rightPagerItems = amountSelectionUiState.fractionalParts,
            onShowInputDialog = onOpenDialog
        )
    }
}

@Composable
fun AmountSelectionDialog(
    selectedCoffee: CoffeeUiState,
    selectedAmount: String,
    onUpdateCoffeeAmountText: (String) -> Unit,
    onHideDialog: () -> Unit
) {
    var amount by rememberSaveable { mutableStateOf("") }

    var isGreaterThanAvailable by rememberSaveable { mutableStateOf(false) }

    val amountErrorMessage = if (isGreaterThanAvailable) {
        stringResource(
            id = R.string.assistant_select_amount_error,
            selectedCoffee.amount.toString()
        )
    } else {
        "\n"
    }

    AlertDialog(
        title = { Text(text = stringResource(id = R.string.assistant_select_amount)) },
        text = {
            FilteredOutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                regex = Regex("([0-9]+([.][0-9]?)?|[.][0-9])"),
                textStyle = MaterialTheme.typography.bodyLarge,
                placeholder = { Text(text = selectedAmount) },
                supportingText = { Text(text = amountErrorMessage) },
                isError = isGreaterThanAvailable,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedCoffeeAmount = selectedCoffee.amount?.toFloat()?.toInt() ?: 0
                    val amountInt = amount.toFloatOrNull()?.toInt() ?: selectedCoffeeAmount

                    isGreaterThanAvailable = amountInt > selectedCoffeeAmount

                    if (!isGreaterThanAvailable) {
                        onHideDialog()
                        onUpdateCoffeeAmountText(amount)
                    }
                },
                content = { Text(text = stringResource(id = R.string.dialog_action_select)) }
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
fun RatioSelectionDialog(
    selectedCoffeeRatio: Int,
    selectedWaterRatio: Int,
    onUpdateRatioText: (String, String) -> Unit,
    onHideDialog: () -> Unit
) {
    var coffeeRatio by rememberSaveable { mutableStateOf("") }
    var waterRatio by rememberSaveable { mutableStateOf("") }

    var isCoffeeRatioWrong by rememberSaveable { mutableStateOf(false) }
    var isWaterRatioWrong by rememberSaveable { mutableStateOf(false) }

    val coffeeRatioErrorMessage = if (isCoffeeRatioWrong) {
        stringResource(id = R.string.assistant_select_ratio_error)
    } else {
        "\n"
    }
    val waterRatioErrorMessage = if (isWaterRatioWrong) {
        stringResource(id = R.string.assistant_select_ratio_error)
    } else {
        "\n"
    }

    AlertDialog(
        title = { Text(text = stringResource(id = R.string.assistant_select_ratio)) },
        text = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilteredOutlinedTextField(
                    value = coffeeRatio,
                    onValueChange = { coffeeRatio = it },
                    regex = Regex("[0-9]+"),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.End),
                    placeholder = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "$selectedCoffeeRatio",
                            style = LocalTextStyle.current.copy(textAlign = TextAlign.End)
                        )
                    },
                    supportingText = {
                        Text(
                            text = coffeeRatioErrorMessage,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    isError = isCoffeeRatioWrong,
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Box(
                    modifier = Modifier.heightIn(min = 56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                FilteredOutlinedTextField(
                    value = waterRatio,
                    onValueChange = { waterRatio = it },
                    regex = Regex("[0-9]+"),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    placeholder = {
                        Text(text = "$selectedWaterRatio")
                    },
                    supportingText = {
                        Text(
                            text = waterRatioErrorMessage,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    isError = isWaterRatioWrong,
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    isCoffeeRatioWrong = coffeeRatio.toIntOrNull() == 0
                    isWaterRatioWrong = waterRatio.toIntOrNull() == 0

                    if (!isCoffeeRatioWrong && !isWaterRatioWrong) {
                        onHideDialog()
                        onUpdateRatioText(coffeeRatio, waterRatio)
                    }
                },
                content = { Text(text = stringResource(id = R.string.dialog_action_select)) }
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

@Preview
@Composable
fun AssistantParametersScreenPreview() {
    val firstSelectedCoffee = CoffeeUiState(
        name = "ethiopia sami",
        brand = "monko.",
        amount = "250.0"
    )
    val uiState = BrewAssistantUiState(
        selectedCoffees = mapOf(
            Pair(firstSelectedCoffee, AmountSelectionUiState(selectedAmount = "1.2"))
        )
    )
    AssistantParametersScreen(
        uiState = uiState,
        onUpdateAmountSelectionWholeNumber = { _, _ -> },
        onUpdateAmountSelectionFractionalPart = { _, _ -> },
        onUpdateAmountSelectionText = { _, _ -> },
        onUpdateHasRatio = {},
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
    val uiState = BrewAssistantUiState(
        selectedCoffees = mapOf(
            Pair(firstSelectedCoffee, AmountSelectionUiState()),
            Pair(secondSelectedCoffee, AmountSelectionUiState(selectedAmount = "1.2"))
        )
    )
    AssistantParametersScreen(
        uiState = uiState,
        onUpdateAmountSelectionWholeNumber = { _, _ -> },
        onUpdateAmountSelectionFractionalPart = { _, _ -> },
        onUpdateAmountSelectionText = { _, _ -> },
        onUpdateHasRatio = {},
        onUpdateCoffeeRatio = {},
        onUpdateRatioText = { _, _ -> }
    ) {}
}

@Preview
@Composable
fun AmountSelectionDialogPreview() {
    val selectedCoffee = CoffeeUiState(
        name = "ethiopia sami",
        brand = "monko.",
        amount = "250.0"
    )
    MyCoffeeTheme(darkTheme = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            AmountSelectionDialog(
                selectedCoffee = selectedCoffee,
                selectedAmount = "0.0",
                onUpdateCoffeeAmountText = {},
                onHideDialog = {}
            )
        }
    }
}

@Preview
@Composable
fun RatioSelectionDialogPreview() {
    MyCoffeeTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            RatioSelectionDialog(
                selectedCoffeeRatio = 1,
                selectedWaterRatio = 1,
                onUpdateRatioText = { _, _ -> },
                onHideDialog = {}
            )
        }
    }
}