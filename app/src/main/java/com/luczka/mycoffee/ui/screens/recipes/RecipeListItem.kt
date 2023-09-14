@file:Suppress("SpellCheckingInspection")

package com.luczka.mycoffee.ui.screens.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.luczka.mycoffee.models.Recipe
import com.luczka.mycoffee.ui.model.RecipeDetailsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListItem(
    recipeCardUiState: RecipeDetailsUiState,
    onClick: () -> Unit,
) {
    Column(modifier = Modifier.clickable { onClick() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 1f)
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
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
                    maxLines = 1,
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