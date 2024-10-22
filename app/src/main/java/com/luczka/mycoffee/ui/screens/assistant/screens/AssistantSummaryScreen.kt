package com.luczka.mycoffee.ui.screens.assistant.screens

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.screens.assistant.AmountSelectionUiState
import com.luczka.mycoffee.ui.screens.assistant.AssistantAction
import com.luczka.mycoffee.ui.screens.assistant.AssistantUiState
import com.luczka.mycoffee.ui.screens.assistant.components.AssistantSummaryCoffeeListItem
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme
import com.luczka.mycoffee.util.hasMultipleElements

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantSummaryScreen(
    uiState: AssistantUiState,
    onAction: (AssistantAction) -> Unit,
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
            is AssistantUiState.NoneSelected -> {
                /* No coffees selected to be displayed */
            }

            is AssistantUiState.CoffeeSelected -> {
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
                    Column {
                        uiState.selectedCoffees.entries.forEach { entry ->
                            AssistantSummaryCoffeeListItem(
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
            Column {
                SummaryParametersListItem(
                    index = 0,
                    headlineText = stringResource(id = R.string.assistant_ratio),
                    trailingText = stringResource(
                        id = R.string.assistant_ratio_format,
                        uiState.ratioSelectionUiState.selectedCoffeeRatio,
                        uiState.ratioSelectionUiState.selectedWaterRatio
                    )
                )
                SummaryParametersListItem(
                    index = 1,
                    headlineText = stringResource(id = R.string.assistant_coffee),
                    trailingText = stringResource(
                        id = R.string.coffee_parameters_amount_with_unit,
                        uiState.selectedAmountsSum
                    )
                )
                SummaryParametersListItem(
                    index = 2,
                    headlineText = stringResource(id = R.string.assistant_water),
                    trailingText = stringResource(
                        id = R.string.coffee_parameters_amount_with_unit,
                        uiState.waterAmount
                    )
                )
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
                text = "Rate brew",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        item {
            val ratings = (0..10).toList().sortedDescending()

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                reverseLayout = true,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(ratings) { index ->
                    val isSelected = uiState.rating == index

                    val cardAlpha = animateFloatAsState(
                        targetValue = if (isSelected) 1f else 0.5f,
                        animationSpec = TweenSpec(durationMillis = 200),
                        label = ""
                    )

                    Card(
                        modifier = Modifier.graphicsLayer { alpha = cardAlpha.value },
                        onClick = {
                            val selectedRating = if (isSelected) null else index
                            val action = AssistantAction.OnRatingChanged(selectedRating)
                            onAction(action)
                        }
                    ) {
                        Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                            Text(text = index.toString())
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
                text = "Notes",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        item {
            val textFieldTextLineHeight = MaterialTheme.typography.bodyLarge.lineHeight.value.dp
            val textFieldVerticalPaddingSum = 32.dp
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(textFieldTextLineHeight * 4 + textFieldVerticalPaddingSum),
                value = uiState.notes,
                onValueChange = { value ->
                    val action = AssistantAction.OnNotesChanged(value)
                    onAction(action)
                },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            )
        }
    }
}

@Composable
fun SummaryParametersListItem(
    index: Int,
    headlineText: String,
    trailingText: String
) {
    ListItem(
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "${index + 1}")
            }
        },
        headlineContent = { Text(text = headlineText) },
        trailingContent = { Text(text = trailingText) }
    )
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
    val uiState = AssistantUiState.CoffeeSelected(
        selectedCoffees = mapOf(
            Pair(firstSelectedCoffee, AmountSelectionUiState()),
            Pair(secondSelectedCoffee, AmountSelectionUiState(selectedAmount = "1.2"))
        )
    )
    MyCoffeeTheme {
        AssistantSummaryScreen(
            uiState = uiState,
            onAction = {}
        )
    }
}