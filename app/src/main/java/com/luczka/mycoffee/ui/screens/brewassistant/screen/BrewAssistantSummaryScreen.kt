package com.luczka.mycoffee.ui.screens.brewassistant.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.icons.PercentIcon
import com.luczka.mycoffee.ui.components.icons.ScaleIcon
import com.luczka.mycoffee.ui.components.icons.TimerIcon
import com.luczka.mycoffee.ui.components.icons.WaterDropIcon
import com.luczka.mycoffee.ui.screens.brewassistant.BrewAssistantAction
import com.luczka.mycoffee.ui.screens.brewassistant.component.AssistantSummaryCoffeeListItem
import com.luczka.mycoffee.ui.screens.brewassistant.component.BrewAssistantSummaryParametersListItem
import com.luczka.mycoffee.ui.screens.brewassistant.state.BrewAssistantUiState

@Composable
fun BrewAssistantSummaryScreen(
    uiState: BrewAssistantUiState,
    onAction: (BrewAssistantAction) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.brew_assistant_summary_screen_title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        when (uiState) {
            is BrewAssistantUiState.NoneSelected -> {
                /* No coffees selected to be displayed */
            }

            is BrewAssistantUiState.CoffeeSelected -> {
                item {
                    Text(
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 8.dp),
                        text = if (uiState.selectedCoffees.size > 1) {
                            stringResource(id = R.string.selected_coffees)
                        } else {
                            stringResource(id = R.string.selected_coffee)
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
                item {
                    Column {
                        uiState.selectedCoffees.forEach { (coffeeUiState, brewAssistantCoffeeAmountItemUiState) ->
                            val integerPart = brewAssistantCoffeeAmountItemUiState.selectedIntegerPart()
                            val fractionalPart = brewAssistantCoffeeAmountItemUiState.selectedFractionalPart()
                            AssistantSummaryCoffeeListItem(
                                coffeeUiState = coffeeUiState,
                                selectedAmount = stringResource(
                                    R.string.format_coffee_integer_part_decimal_part_grams,
                                    integerPart,
                                    fractionalPart
                                )
                            )
                        }
                    }
                }
            }
        }
        item {
            Text(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                text = stringResource(id = R.string.assistant_selected_parameters),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        item {
            Column {
                BrewAssistantSummaryParametersListItem(
                    icon = {
                        PercentIcon()
                    },
                    headlineText = stringResource(id = R.string.ratio),
                    trailingText = stringResource(
                        id = R.string.format_ratio,
                        uiState.brewAssistantRatioItemUiState.selectedCoffeeRatio(),
                        uiState.brewAssistantRatioItemUiState.selectedWaterRatio()
                    )
                )
                BrewAssistantSummaryParametersListItem(
                    icon = {
                        ScaleIcon()
                    },
                    headlineText = stringResource(id = R.string.coffee),
                    trailingText = stringResource(
                        id = R.string.format_coffee_amount_grams,
                        uiState.selectedAmountsSum
                    )
                )
                BrewAssistantSummaryParametersListItem(
                    icon = {
                        WaterDropIcon()
                    },
                    headlineText = stringResource(id = R.string.water),
                    trailingText = stringResource(
                        id = R.string.format_coffee_amount_grams,
                        uiState.waterAmount
                    )
                )
            }
        }
        item {
            Text(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                text = "Brewing time",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        item {
            BrewAssistantSummaryParametersListItem(
                icon = {
                    TimerIcon()
                },
                headlineText = "Time",
                trailingText = uiState.brewAssistantTimerItemUiState.formattedTime()
            )
        }
    }
}