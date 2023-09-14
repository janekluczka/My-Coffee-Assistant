package com.luczka.mycoffee.ui.screens.methods

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class MethodUiState(
    val id: String = "",
    val title: String = "",
    val url: String? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MethodCard(
    onClick: () -> Unit,
    methodUiState: MethodUiState
) {
    Card(onClick = onClick) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 2f)
                .padding(16.dp),
        ) {
                Text(
                    text = methodUiState.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )
        }
    }
}

@Preview
@Composable
fun MethodCardPreview() {
    val methodUiState = MethodUiState(title = "Aeropress")
    MethodCard(
        onClick = {},
        methodUiState = methodUiState
    )
}