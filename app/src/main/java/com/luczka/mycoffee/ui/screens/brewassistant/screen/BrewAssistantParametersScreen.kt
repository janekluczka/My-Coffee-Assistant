package com.luczka.mycoffee.ui.screens.brewassistant.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.custom.doubleverticalpager.DoubleVerticalPager
import com.luczka.mycoffee.ui.components.icons.ArrowDropDownIcon
import com.luczka.mycoffee.ui.components.icons.ArrowDropUpIcon
import com.luczka.mycoffee.ui.components.icons.PauseCircleIcon
import com.luczka.mycoffee.ui.components.icons.PercentIcon
import com.luczka.mycoffee.ui.components.icons.PlayCircleIcon
import com.luczka.mycoffee.ui.components.icons.ScaleIcon
import com.luczka.mycoffee.ui.components.icons.StopCircleIcon
import com.luczka.mycoffee.ui.components.icons.TimerIcon
import com.luczka.mycoffee.ui.components.icons.WaterDropIcon
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.screens.brewassistant.BrewAssistantAction
import com.luczka.mycoffee.ui.screens.brewassistant.component.BrewAssistantParametersExpandableListItem
import com.luczka.mycoffee.ui.screens.brewassistant.component.BrewAssistantParametersListItem
import com.luczka.mycoffee.ui.screens.brewassistant.state.BrewAssistantCoffeeAmountItemUiState
import com.luczka.mycoffee.ui.screens.brewassistant.state.BrewAssistantRatioItemUiState
import com.luczka.mycoffee.ui.screens.brewassistant.state.BrewAssistantTimerItemUiState
import com.luczka.mycoffee.ui.screens.brewassistant.state.BrewAssistantUiState
import com.luczka.mycoffee.ui.theme.MyCoffeeTypography

@Composable
fun BrewAssistantParametersScreen(
    uiState: BrewAssistantUiState,
    onAction: (BrewAssistantAction) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
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
            if (uiState.brewAssistantRatioItemUiState.openPicker) {
                // TODO: Add ratio dialog
            }

            RatioSelectionListItemWithPicker(
                brewAssistantRatioItemUiState = uiState.brewAssistantRatioItemUiState,
                onClick = {
                    val action = BrewAssistantAction.OnRatioSelectionItemClicked
                    onAction(action)
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
                    if (uiState.defaultBrewAssistantAmountUiState.openDialog) {
                        // TODO: Add Coffee amount dialog
                    }

                    AmountSelectionListItemWithPicker(
                        selectedCoffee = null,
                        brewAssistantCoffeeAmountItemUiState = uiState.defaultBrewAssistantAmountUiState,
                        onClick = {
                            val action = BrewAssistantAction.OnAmountSelectionItemClicked(coffeeUiState = null)
                            onAction(action)
                        },
                        onLeftPagerIndexChanged = { index ->
                            val action = BrewAssistantAction.OnAmountSelectionIntegerPartIndexChanged(coffeeUiState = null, leftPagerPageIndex = index)
                            onAction(action)
                        },
                        onRightPagerIndexChanged = { index ->
                            val action = BrewAssistantAction.OnAmountSelectionFractionalPartIndexChanged(coffeeUiState = null, rightPagerPageIndex = index)
                            onAction(action)
                        }
                    )
                }
            }

            is BrewAssistantUiState.CoffeeSelected -> {
                uiState.selectedCoffees.forEach { (selectedCoffee, brewAssistantCoffeeAmountItemUiState) ->
                    item {
                        if (brewAssistantCoffeeAmountItemUiState.openDialog) {
                            // TODO: Add Coffee amount dialog
                        }

                        AmountSelectionListItemWithPicker(
                            selectedCoffee = selectedCoffee,
                            brewAssistantCoffeeAmountItemUiState = brewAssistantCoffeeAmountItemUiState,
                            onClick = {
                                val action = BrewAssistantAction.OnAmountSelectionItemClicked(coffeeUiState = selectedCoffee)
                                onAction(action)
                            },
                            onLeftPagerIndexChanged = { index ->
                                val action = BrewAssistantAction.OnAmountSelectionIntegerPartIndexChanged(coffeeUiState = selectedCoffee, leftPagerPageIndex = index)
                                onAction(action)
                            },
                            onRightPagerIndexChanged = { index ->
                                val action = BrewAssistantAction.OnAmountSelectionFractionalPartIndexChanged(coffeeUiState = selectedCoffee, rightPagerPageIndex = index)
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
            if (uiState.brewAssistantTimerItemUiState.openDialog) {
                // TODO: Add timer dialog
            }

            TimeSelectionListItemWithPicker(
                brewAssistantTimerItemUiState = uiState.brewAssistantTimerItemUiState,
                onClick = {
                    val action = BrewAssistantAction.OnTimeSelectionItemClicked
                    onAction(action)
                },
                onStartStopTimerClicked = {
                    val action = BrewAssistantAction.OnStartStopTimerClicked
                    onAction(action)
                },
                onResetTimerClicked = {
                    val action = BrewAssistantAction.OnResetTimerClicked
                    onAction(action)
                },
                onLeftPagerIndexChanged = { index ->
                    val action = BrewAssistantAction.OnTimeSelectionMinutesIndexChanged(leftPagerPageIndex = index)
                    onAction(action)
                },
                onRightPagerIndexChanged = { index ->
                    val action = BrewAssistantAction.OnTimeSelectionSecondsIndexChanged(rightPagerPageIndex = index)
                    onAction(action)
                }
            )
        }
    }
}

@Composable
private fun RatioSelectionListItemWithPicker(
    brewAssistantRatioItemUiState: BrewAssistantRatioItemUiState,
    onClick: () -> Unit,
    onLeftPagerIndexChanged: (Int) -> Unit,
    onRightPagerIndexChanged: (Int) -> Unit
) {
    val coffeeRatio = brewAssistantRatioItemUiState.selectedCoffeeRatio()
    val waterRatio = brewAssistantRatioItemUiState.selectedWaterRatio()
    val separatorText = stringResource(id = brewAssistantRatioItemUiState.separatorRes)

    BrewAssistantParametersExpandableListItem(
        onClick = onClick,
        icon = {
            PercentIcon()
        },
        overlineText = stringResource(id = R.string.ratio),
        headlineText = "$coffeeRatio$separatorText$waterRatio",
        expanded = brewAssistantRatioItemUiState.openPicker
    ) {
        DoubleVerticalPager(
            leftPagerPageIndex = brewAssistantRatioItemUiState.coffeeRatioIndex,
            leftPagerItems = brewAssistantRatioItemUiState.coffeeRatioItems,
            leftPagerItemsTextFormatter = brewAssistantRatioItemUiState.coffeeRatioItemsTextFormatter,
            rightPagerPageIndex = brewAssistantRatioItemUiState.waterRatioIndex,
            rightPagerItems = brewAssistantRatioItemUiState.waterRatioItems,
            rightPagerItemsTextFormatter = brewAssistantRatioItemUiState.waterRatioItemsTextFormatter,
            separatorRes = brewAssistantRatioItemUiState.separatorRes,
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
    brewAssistantCoffeeAmountItemUiState: BrewAssistantCoffeeAmountItemUiState,
    onClick: () -> Unit,
    onLeftPagerIndexChanged: (Int) -> Unit,
    onRightPagerIndexChanged: (Int) -> Unit
) {
    val integerPart = brewAssistantCoffeeAmountItemUiState.selectedIntegerPart()
    val fractionalPart = brewAssistantCoffeeAmountItemUiState.selectedFractionalPart()
    val separatorText = stringResource(id = brewAssistantCoffeeAmountItemUiState.separatorRes)

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
        headlineText = "$integerPart$separatorText$fractionalPart",
        expanded = brewAssistantCoffeeAmountItemUiState.openPicker
    ) {
        DoubleVerticalPager(
            leftPagerPageIndex = brewAssistantCoffeeAmountItemUiState.leftPagerPageIndex,
            leftPagerItems = brewAssistantCoffeeAmountItemUiState.leftPagerItems,
            leftPagerItemsTextFormatter = brewAssistantCoffeeAmountItemUiState.leftPagerItemsTextFormatter,
            rightPagerPageIndex = brewAssistantCoffeeAmountItemUiState.rightPagerPageIndex,
            rightPagerItems = brewAssistantCoffeeAmountItemUiState.rightPagerItems,
            rightPagerItemsTextFormatter = brewAssistantCoffeeAmountItemUiState.rightPagerItemsTextFormatter,
            separatorRes = brewAssistantCoffeeAmountItemUiState.separatorRes,
            textStyle = MaterialTheme.typography.displayLarge.copy(
                fontFamily = MyCoffeeTypography.redditMonoFontFamily,
            ),
            onLeftPagerIndexChanged = onLeftPagerIndexChanged,
            onRightPagerIndexChanged = onRightPagerIndexChanged
        )
    }
}

@Composable
private fun TimeSelectionListItemWithPicker(
    brewAssistantTimerItemUiState: BrewAssistantTimerItemUiState,
    onClick: () -> Unit,
    onStartStopTimerClicked: () -> Unit,
    onResetTimerClicked: () -> Unit,
    onLeftPagerIndexChanged: (Int) -> Unit,
    onRightPagerIndexChanged: (Int) -> Unit
) {
    BrewAssistantParametersListItem(
        modifier = Modifier.clickable(onClick = onClick),
        icon = {
            TimerIcon()
        },
        overlineText = "Time",
        headlineText = brewAssistantTimerItemUiState.formattedTime(),
        trailingContent = {
            Row(
                modifier = Modifier.offset(x = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onStartStopTimerClicked) {
                    if (brewAssistantTimerItemUiState.isRunning) {
                        PauseCircleIcon()
                    } else {
                        PlayCircleIcon()
                    }
                }
                IconButton(onClick = onResetTimerClicked) {
                    StopCircleIcon()
                }
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (brewAssistantTimerItemUiState.openPicker) {
                        ArrowDropUpIcon()
                    } else {
                        ArrowDropDownIcon()
                    }
                }
            }
        }
    )
    AnimatedVisibility(
        visible = brewAssistantTimerItemUiState.openPicker,
        enter = expandVertically(),
        exit = shrinkVertically(),
        content = {
            DoubleVerticalPager(
                leftPagerPageIndex = brewAssistantTimerItemUiState.minutesPageIndex,
                leftPagerItems = brewAssistantTimerItemUiState.minutesPagerItems,
                leftPagerItemsTextFormatter = brewAssistantTimerItemUiState.minutesPagerItemsTextFormatter,
                rightPagerPageIndex = brewAssistantTimerItemUiState.secondsPageIndex,
                rightPagerItems = brewAssistantTimerItemUiState.secondsPagerItems,
                rightPagerItemsTextFormatter = brewAssistantTimerItemUiState.secondsPagerItemsTextFormatter,
                separatorRes = brewAssistantTimerItemUiState.separatorRes,
                userScrollEnabled = !brewAssistantTimerItemUiState.isRunning,
                textStyle = MaterialTheme.typography.displayLarge.copy(
                    fontFamily = MyCoffeeTypography.redditMonoFontFamily,
                ),
                onLeftPagerIndexChanged = onLeftPagerIndexChanged,
                onRightPagerIndexChanged = onRightPagerIndexChanged
            )
        }
    )
}