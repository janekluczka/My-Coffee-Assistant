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
import com.luczka.mycoffee.ui.components.custom.doubleverticalpager.DoubleVerticalPager
import com.luczka.mycoffee.ui.components.custom.doubleverticalpager.DoubleVerticalPagerState
import com.luczka.mycoffee.ui.components.icons.KeyboardIcon
import com.luczka.mycoffee.ui.components.icons.PauseCircleIcon
import com.luczka.mycoffee.ui.components.icons.PercentIcon
import com.luczka.mycoffee.ui.components.icons.PlayCircleIcon
import com.luczka.mycoffee.ui.components.icons.ScaleIcon
import com.luczka.mycoffee.ui.components.icons.StopCircleIcon
import com.luczka.mycoffee.ui.components.icons.TimerIcon
import com.luczka.mycoffee.ui.components.icons.WaterDropIcon
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.screens.brewassistant.BrewAssistantAction
import com.luczka.mycoffee.ui.screens.brewassistant.BrewAssistantUiState
import com.luczka.mycoffee.ui.screens.brewassistant.components.BrewAssistantParametersExpandableListItem
import com.luczka.mycoffee.ui.screens.brewassistant.components.BrewAssistantParametersListItem
import com.luczka.mycoffee.ui.theme.MyCoffeeTypography

@Composable
fun BrewAssistantParametersScreen(
    uiState: BrewAssistantUiState,
    onAction: (BrewAssistantAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.brew_assistant_parameters_screen_title),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        item {
            Text(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                text = "Recipe (optional)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        item {
            ListItem(
                modifier = Modifier.clickable {
                    val action = BrewAssistantAction.OnSelectRecipeClicked
                    onAction(action)
                },
                headlineContent = {
                    Text("Select recipe")
                }
            )
        }
        item {
            Text(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                text = "Brew parameters",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        item {
            var openPicker by rememberSaveable { mutableStateOf(false) } // TODO: Move state to uiState
            var openDialog by rememberSaveable { mutableStateOf(false) } // TODO: Move state to uiState

            if (openDialog) {
                // TODO: Add ratio dialog
            }

            RatioSelectionListItemWithPicker(
                ratioDoubleVerticalPagerState = uiState.ratioSelectionUiState,
                openPicker = openPicker,
                onClick = {
                    openPicker = !openPicker
                },
                onLeftPagerIndexChanged = { index ->
                    val action = BrewAssistantAction.OnRatioSelectionCoffeeIndexChanged(leftPagerPageIndex = index)
                    onAction(action)
                },
                onRightPagerIndexChanged = { index ->
                    val action = BrewAssistantAction.OnRatioSelectionWaterIndexChanged(rightPagerPageIndex = index)
                    onAction(action)
                }
            )
        }
        when (uiState) {
            is BrewAssistantUiState.NoneSelected -> {
                item {
                    var openAmountPicker by rememberSaveable { mutableStateOf(false) } // TODO: Move state to uiState
                    var openAmountDialog by rememberSaveable { mutableStateOf(false) } // TODO: Move state to uiState

                    if (openAmountDialog) {
                        // TODO: Add Coffee amount dialog
                    }

                    AmountSelectionListItemWithPicker(
                        selectedCoffee = null,
                        amountDoubleVerticalPagerState = uiState.defaultAmountDoubleVerticalPagerState,
                        openPicker = openAmountPicker,
                        onClick = {
                            openAmountPicker = !openAmountPicker
                        },
                        onLeftPagerIndexChanged = { index ->
                            val action = BrewAssistantAction.OnAmountSelectionIntegerPartIndexChanged(key = null, leftPagerPageIndex = index)
                            onAction(action)
                        },
                        onRightPagerIndexChanged = { index ->
                            val action = BrewAssistantAction.OnAmountSelectionFractionalPartIndexChanged(key = null, rightPagerPageIndex = index)
                            onAction(action)
                        }
                    )
                }
            }

            is BrewAssistantUiState.CoffeeSelected -> {
                uiState.selectedCoffees.forEach { (selectedCoffee, amountDoubleVerticalPagerState) ->
                    item {
                        var openAmountPicker by rememberSaveable { mutableStateOf(false) } // TODO: Move state to uiState
                        var openAmountDialog by rememberSaveable { mutableStateOf(false) } // TODO: Move state to uiState

                        if (openAmountDialog) {
                            // TODO: Add Coffee amount dialog
                        }

                        AmountSelectionListItemWithPicker(
                            selectedCoffee = selectedCoffee,
                            amountDoubleVerticalPagerState = amountDoubleVerticalPagerState,
                            openPicker = openAmountPicker,
                            onClick = {
                                openAmountPicker = !openAmountPicker
                            },
                            onLeftPagerIndexChanged = { index ->
                                val action = BrewAssistantAction.OnAmountSelectionIntegerPartIndexChanged(key = selectedCoffee, leftPagerPageIndex = index)
                                onAction(action)
                            },
                            onRightPagerIndexChanged = { index ->
                                val action = BrewAssistantAction.OnAmountSelectionFractionalPartIndexChanged(key = selectedCoffee, rightPagerPageIndex = index)
                                onAction(action)
                            }
                        )
                    }
                }
            }
        }
        item {
            BrewAssistantParametersListItem(
                icon = {
                    WaterDropIcon()
                },
                overlineText = stringResource(id = R.string.water),
                headlineText = stringResource(
                    id = R.string.format_coffee_amount_grams,
                    uiState.waterAmount
                )
            )
        }
        item {
            Text(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                text = "Brewing time (optional)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        item {
            var openTimerDialog by rememberSaveable { mutableStateOf(false) } // TODO: Move state to uiState

            if (openTimerDialog) {
                // TODO: Add timer dialog
            }

            BrewAssistantParametersListItem(
                icon = {
                    TimerIcon()
                },
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
                                val action = BrewAssistantAction.OnStartStopTimerClicked
                                onAction(action)
                            }
                        ) {
                            if (uiState.isTimerRunning) {
                                PauseCircleIcon()
                            } else {
                                PlayCircleIcon()
                            }
                        }
                        IconButton(
                            onClick = {
                                val action = BrewAssistantAction.OnResetTimerClicked
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
    onLeftPagerIndexChanged: (Int) -> Unit,
    onRightPagerIndexChanged: (Int) -> Unit
) {
    val coffeeRatio = ratioDoubleVerticalPagerState.currentLeftPagerItem()
    val waterRatio = ratioDoubleVerticalPagerState.currentRightPagerItem()

    BrewAssistantParametersExpandableListItem(
        onClick = onClick,
        icon = {
            PercentIcon()
        },
        overlineText = stringResource(id = R.string.ratio),
        headlineText = stringResource(id = R.string.format_ratio, coffeeRatio, waterRatio),
        expanded = openPicker
    ) {
        DoubleVerticalPager(
            doubleVerticalPagerState = ratioDoubleVerticalPagerState,
            textStyle = MaterialTheme.typography.displayLarge.copy(
                fontFamily = MyCoffeeTypography.redditMonoFontFamily,
            ),
            onLeftPagerIndexChanged = onLeftPagerIndexChanged,
            onRightPagerIndexChanged = onRightPagerIndexChanged
        )
    }
}

@Composable
private fun AmountSelectionListItemWithPicker(
    selectedCoffee: CoffeeUiState?,
    amountDoubleVerticalPagerState: DoubleVerticalPagerState,
    openPicker: Boolean,
    onClick: () -> Unit,
    onLeftPagerIndexChanged: (Int) -> Unit,
    onRightPagerIndexChanged: (Int) -> Unit
) {
    val integerPart = amountDoubleVerticalPagerState.currentLeftPagerItem()
    val fractionalPart = amountDoubleVerticalPagerState.currentRightPagerItem()

    val overlineText = if (selectedCoffee == null) {
        stringResource(id = R.string.coffee)
    } else {
        stringResource(id = R.string.format_coffee_name_coma_brand, selectedCoffee.originOrName, selectedCoffee.roasterOrBrand)
    }

    BrewAssistantParametersExpandableListItem(
        onClick = onClick,
        icon = {
            ScaleIcon()
        },
        overlineText = overlineText,
        headlineText = stringResource(id = R.string.format_coffee_integer_part_decimal_part_grams, integerPart, fractionalPart),
        expanded = openPicker
    ) {
        DoubleVerticalPager(
            doubleVerticalPagerState = amountDoubleVerticalPagerState,
            textStyle = MaterialTheme.typography.displayLarge.copy(
                fontFamily = MyCoffeeTypography.redditMonoFontFamily,
            ),
            onLeftPagerIndexChanged = onLeftPagerIndexChanged,
            onRightPagerIndexChanged = onRightPagerIndexChanged
        )
    }
}