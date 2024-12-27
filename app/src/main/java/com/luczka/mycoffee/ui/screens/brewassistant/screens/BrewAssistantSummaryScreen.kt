package com.luczka.mycoffee.ui.screens.brewassistant.screens

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
import com.luczka.mycoffee.ui.screens.brewassistant.BrewAssistantAction
import com.luczka.mycoffee.ui.screens.brewassistant.BrewAssistantUiState
import com.luczka.mycoffee.ui.screens.brewassistant.components.AssistantSummaryCoffeeListItem
import com.luczka.mycoffee.ui.screens.brewassistant.components.BrewAssistantSummaryParametersListItem

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
                text = stringResource(id = R.string.assistant_summary_screen_title),
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
                        modifier = Modifier.padding(
                            top = 24.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 8.dp
                        ),
                        text = if (uiState.selectedCoffees.size > 1) {
                            stringResource(id = R.string.assistant_selected_coffees)
                        } else {
                            stringResource(id = R.string.assistant_selected_coffee)
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
                item {
                    Column {
                        uiState.selectedCoffees.entries.forEach { entry ->
                            val integerPart = entry.value.currentLeftPagerItem()
                            val fractionalPart = entry.value.currentRightPagerItem()
                            AssistantSummaryCoffeeListItem(
                                coffeeUiState = entry.key,
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
                modifier = Modifier.padding(
                    top = 24.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp
                ),
                text = stringResource(id = R.string.assistant_selected_parameters),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        item {
            Column {
                BrewAssistantSummaryParametersListItem(
                    index = 0,
                    headlineText = stringResource(id = R.string.assistant_ratio),
                    trailingText = stringResource(
                        id = R.string.format_ratio,
                        uiState.ratioSelectionUiState.currentLeftPagerItem(),
                        uiState.ratioSelectionUiState.currentRightPagerItem()
                    )
                )
                BrewAssistantSummaryParametersListItem(
                    index = 1,
                    headlineText = stringResource(id = R.string.assistant_coffee),
                    trailingText = stringResource(
                        id = R.string.format_coffee_amount_grams,
                        uiState.selectedAmountsSum
                    )
                )
                BrewAssistantSummaryParametersListItem(
                    index = 2,
                    headlineText = stringResource(id = R.string.assistant_water),
                    trailingText = stringResource(
                        id = R.string.format_coffee_amount_grams,
                        uiState.waterAmount
                    )
                )
            }
        }
    }
}