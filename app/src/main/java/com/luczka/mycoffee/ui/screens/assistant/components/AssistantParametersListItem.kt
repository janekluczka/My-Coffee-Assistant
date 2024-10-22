package com.luczka.mycoffee.ui.screens.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.ui.theme.MyCoffeeTypography

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AssistantParametersListItem(
    modifier: Modifier = Modifier,
    index: Int,
    overlineText: String,
    headlineText: String,
    trailingContent: @Composable (() -> Unit)? = null,
    colors: ListItemColors = ListItemDefaults.colors(),
    tonalElevation: Dp = ListItemDefaults.Elevation,
    shadowElevation: Dp = ListItemDefaults.Elevation,
) {
    ListItem(
        modifier = modifier,
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
        overlineContent = {
            Text(text = overlineText)
        },
        headlineContent = {
            Text(
                text = headlineText,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = MyCoffeeTypography.redditMonoFontFamily
                )
            )
        },
        trailingContent = trailingContent,
        colors = colors,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation
    )
}
