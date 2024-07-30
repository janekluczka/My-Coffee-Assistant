@file:Suppress("SpellCheckingInspection")

package com.luczka.mycoffee.ui.component.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.luczka.mycoffee.model.Recipe
import com.luczka.mycoffee.ui.model.RecipeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListItem(
    recipeCardUiState: RecipeUiState,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
                .width(114.dp)
                .height(64.dp)
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = recipeCardUiState.imageUrl,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
        ListItem(
            headlineText = {
                Text(
                    text = recipeCardUiState.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
            supportingText = {
                Text(
                    text = recipeCardUiState.author,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
    }
}

@Preview
@Composable
fun RecipeListItemPreview() {
    val recipeCardUiState = Recipe(
        title = "Rick Astley - Never Gonna Give You Up (Official Music Video)",
        author = "Rick Astley",
        youtubeId = "dQw4w9WgXcQ"
    ).toRecipeDetailsUiState()
    RecipeListItem(recipeCardUiState = recipeCardUiState, onClick = {})
}