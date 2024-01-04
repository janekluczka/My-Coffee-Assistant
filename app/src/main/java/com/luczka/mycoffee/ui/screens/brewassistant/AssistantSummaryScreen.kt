package com.luczka.mycoffee.ui.screens.brewassistant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.SelectedCoffeeCard
import com.luczka.mycoffee.ui.model.CoffeeUiState
import com.luczka.mycoffee.util.hasMultipleElements

@Composable
fun AssistantSummaryScreen(uiState: BrewAssistantUiState) {
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
                        text = if (uiState.selectedCoffees.hasMultipleElements()) {
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
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.selectedCoffees.entries.forEach { entry ->
                            SelectedCoffeeCard(
                                coffeeUiState = entry.key,
                                selectedAmount = entry.value.selectedAmount
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
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BrewParameterCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    parameterName = stringResource(id = R.string.assistant_coffee),
                    parameterValue = stringResource(
                        id = R.string.coffee_parameters_amount_with_unit,
                        uiState.selectedAmountsSum
                    )
                )
                BrewParameterCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    parameterName = stringResource(id = R.string.assistant_ratio),
                    parameterValue = stringResource(
                        id = R.string.assistant_ratio_format,
                        uiState.ratioSelectionUiState.selectedCoffeeRatio,
                        uiState.ratioSelectionUiState.selectedWaterRatio
                    )
                )
                BrewParameterCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    parameterName = stringResource(id = R.string.assistant_water),
                    parameterValue = stringResource(
                        id = R.string.coffee_parameters_amount_with_unit,
                        uiState.waterAmount
                    )
                )
            }
        }
    }
}

@Composable
private fun BrewParameterCard(
    modifier: Modifier,
    parameterName: String,
    parameterValue: String
) {
    OutlinedCard(
        modifier = modifier,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = parameterValue,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = parameterName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun AssistantSummaryScreenPreview() {
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
    AssistantSummaryScreen(uiState = uiState)
}