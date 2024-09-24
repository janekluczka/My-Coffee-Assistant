package com.luczka.mycoffee.ui.components.listitem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ExpandableBrewParametersListItem(
    onClick: () -> Unit,
    index: Int,
    overlineText: String,
    headlineText: String,
    expanded: Boolean,
    expandableContent: @Composable AnimatedVisibilityScope.() -> Unit
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
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
        overlineText = {
            Text(text = overlineText)
        },
        headlineText = {
            Text(text = headlineText)
        },
        trailingContent = {
            if (expanded) {
                Icon(painter = painterResource(id = R.drawable.arrow_drop_up_24px), contentDescription = null)
            } else {
                Icon(painter = painterResource(id = R.drawable.arrow_drop_down_24px), contentDescription = null)
            }
        }
    )
    AnimatedVisibility(
        visible = expanded,
        enter = expandVertically(),
        exit = shrinkVertically(),
        content = expandableContent
    )
}