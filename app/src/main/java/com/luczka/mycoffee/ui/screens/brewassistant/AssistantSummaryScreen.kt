package com.luczka.mycoffee.ui.screens.brewassistant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.model.CoffeeUiState
import com.luczka.mycoffee.ui.screens.brewassistant.components.ScreenTitle
import com.luczka.mycoffee.ui.screens.brewassistant.components.SectionSpacer
import com.luczka.mycoffee.ui.screens.brewassistant.components.SectionTitle
import com.luczka.mycoffee.ui.screens.brewassistant.components.SelectedCoffeeListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantSummaryScreen(uiState: BrewAssistantUiState) {
    if (uiState.selectedCoffees.isEmpty()) return

    val moreThanOneCoffeeSelected = uiState.selectedCoffees.size > 1

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            ScreenTitle(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.assistant_screen_3_title)
            )
        }
        item {
            SectionSpacer()
        }
        item {
            val title = if (uiState.selectedCoffees.size == 1) {
                stringResource(id = R.string.assistant_selected_coffee)
            } else {
                stringResource(id = R.string.assistant_selected_coffees)
            }
            SectionTitle(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = title
            )
        }
        uiState.selectedCoffees.entries.forEachIndexed { index, entry ->
            item {
                val selectedCoffee = entry.key
                val amountSelectionUiState = entry.value
                SelectedCoffeeListItem(
                    index = index,
                    showCoffeeIndex = moreThanOneCoffeeSelected,
                    coffeeUiState = selectedCoffee,
                    selectedAmount = amountSelectionUiState.selectedAmount
                )
            }
        }
        item {
            SectionSpacer()
        }
        item {
            SectionTitle(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Selected parameters"
            )
        }
        item {
            ListItem(
                overlineText = {
                    Text(text = stringResource(id = R.string.assistant_selected_amount))
                },
                headlineText = {
                    Text(
                        text = stringResource(
                            id = R.string.coffee_parameters_amount_with_unit,
                            uiState.selectedAmountsSum
                        )
                    )
                }
            )
        }
        item {
            ListItem(
                overlineText = {
                    Text(text = stringResource(id = R.string.assistant_selected_ratio))
                },
                headlineText = {
                    Text(
                        text = stringResource(
                            id = R.string.assistant_ratio_format,
                            uiState.ratioSelectionUiState.selectedCoffeeRatio,
                            uiState.ratioSelectionUiState.selectedWaterRatio
                        )
                    )
                }
            )
        }
        item {
            ListItem(
                overlineText = {
                    Text(text = stringResource(id = R.string.assistant_water_amount))
                },
                headlineText = {
                    Text(
                        text = stringResource(
                            id = R.string.coffee_parameters_amount_with_unit,
                            uiState.waterAmount
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun BrewParameterCard(
    overlineText: String,
    headlineText: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 3.dp
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .heightIn(min = 72.dp)
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 24.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = overlineText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = headlineText,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
    val uiState = BrewAssistantUiState(
        selectedCoffees = mapOf(
            Pair(firstSelectedCoffee, AmountSelectionUiState()),
            Pair(secondSelectedCoffee, AmountSelectionUiState(selectedAmount = "1.2"))
        )
    )
    AssistantSummaryScreen(uiState = uiState)
}