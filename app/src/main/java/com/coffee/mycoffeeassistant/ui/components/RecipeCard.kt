@file:Suppress("SpellCheckingInspection")

package com.coffee.mycoffeeassistant.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.coffee.mycoffeeassistant.models.Recipe
import com.coffee.mycoffeeassistant.ui.model.components.RecipeCardUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCard(
    onClick: () -> Unit,
    recipeCardUiState: RecipeCardUiState
) {
    Card(onClick = onClick) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = recipeCardUiState.url,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = recipeCardUiState.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )
                if (recipeCardUiState.body != null) {
                    Text(
                        text = recipeCardUiState.body,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun RecipeCardPreview() {
    val recipeCardUiState = Recipe(
        title = "Rick Astley - Never Gonna Give You Up (Official Music Video)",
        author = "Rick Astley",
        youtubeId = "dQw4w9WgXcQ"
    ).toRecipeCardUiState()
    RecipeCard(
        recipeCardUiState = recipeCardUiState,
        onClick = {}
    )
}

@Preview
@Composable
fun RecipeCardWithoutBodyPreview() {
    val recipeCardUiState = Recipe(
        title = "Rick Astley - Never Gonna Give You Up (Official Music Video)",
        author = "",
        youtubeId = "dQw4w9WgXcQ"
    ).toRecipeCardUiState()
    RecipeCard(
        recipeCardUiState = recipeCardUiState,
        onClick = {}
    )
}