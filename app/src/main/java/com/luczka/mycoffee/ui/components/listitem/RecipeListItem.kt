@file:Suppress("SpellCheckingInspection")

package com.luczka.mycoffee.ui.components.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.luczka.mycoffee.ui.models.RecipeUiState

@Composable
fun RecipeListItem(
    modifier: Modifier = Modifier,
    recipeCardUiState: RecipeUiState,
    onClick: () -> Unit,
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        leadingContent = {
            Box(
                modifier = Modifier
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
        },
        overlineContent = {
            Text(
                text = recipeCardUiState.author,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        headlineContent = {
            Text(
                text = recipeCardUiState.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        supportingContent = {
            Text(
                text = "Recipe short description",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}

//@Preview
//@Composable
//fun RecipeListItemPreview() {
//    val recipeCardUiState = RecipeDto(
//        title = "Rick Astley - Never Gonna Give You Up (Official Music Video)",
//        author = "Rick Astley",
//        youtubeId = "dQw4w9WgXcQ"
//    ).toRecipeDetailsUiState()
//    RecipeListItem(recipeCardUiState = recipeCardUiState, onClick = {})
//}