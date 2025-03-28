package com.luczka.mycoffee.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.ui.models.CategoryUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MethodCard(
    modifier: Modifier = Modifier,
    categoryUiState: CategoryUiState,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier,
        onClick = onClick,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 2f)
                .padding(16.dp),
        ) {
            Text(
                text = categoryUiState.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview
@Composable
private fun MethodCardPreview() {
    val categoryUiState = CategoryUiState(name = "Aeropress")
    MethodCard(
        categoryUiState = categoryUiState,
        onClick = {}
    )
}