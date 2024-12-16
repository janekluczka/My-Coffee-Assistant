package com.luczka.mycoffee.ui.components.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BrewDetailsParametersListItem(
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