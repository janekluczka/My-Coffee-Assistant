package com.luczka.mycoffee.ui.screens.brewassistant

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.model.CoffeeUiState
import com.luczka.mycoffee.ui.screens.brewassistant.components.CoffeeSelectionListItem
import com.luczka.mycoffee.ui.screens.brewassistant.components.ScreenTitle
import com.luczka.mycoffee.ui.screens.brewassistant.components.SectionSpacer
import com.luczka.mycoffee.ui.screens.brewassistant.components.SectionTitle

@Composable
fun AssistantSelectionScreen(
    uiState: BrewAssistantUiState,
    onCoffeeSelected: (CoffeeUiState) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            ScreenTitle(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.assistant_screen_1_title)
            )
        }
        item { SectionSpacer() }
        item {
            SectionTitle(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Available coffees"
            )
        }
        items(
            items = uiState.currentCoffees,
            key = { it.id }
        ) { coffeeUiState ->
            CoffeeSelectionListItem(
                coffeeUiState = coffeeUiState,
                isSelected = uiState.selectedCoffees.containsKey(coffeeUiState),
                onCoffeeSelected = onCoffeeSelected
            )
        }
    }
}

@Preview
@Composable
fun AssistantSelectionScreenPreview() {
    val firstSelectedCoffee = CoffeeUiState(
        id = 1,
        name = "ethiopia sami",
        brand = "monko.",
        amount = "250.0"
    )
    val secondSelectedCoffee = CoffeeUiState(
        id = 2,
        name = "Kolumbia",
        brand = "Mała Czarna",
        amount = "200.0"
    )
    val uiState = BrewAssistantUiState(
        currentCoffees = listOf(firstSelectedCoffee, secondSelectedCoffee),
        selectedCoffees = mapOf(Pair(firstSelectedCoffee, AmountSelectionUiState()))
    )
    AssistantSelectionScreen(uiState = uiState, onCoffeeSelected = {})
}