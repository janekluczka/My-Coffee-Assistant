package com.luczka.mycoffee.ui.screens.recipedetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.icons.ArrowBackIcon
import com.luczka.mycoffee.ui.components.listitem.BrewingStepListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    widthSizeClass: WindowWidthSizeClass,
    uiState: RecipeDetailsUiState,
    onAction: (RecipeDetailsAction) -> Unit
) {
    val context = LocalContext.current

    var openDeleteDialog by rememberSaveable { mutableStateOf(false) }

    if (openDeleteDialog) {
        RecipeDetailsLeaveApplicationDialog(
            onNegative = {
                openDeleteDialog = false
            },
            onPositive = {
                openDeleteDialog = false
                onOpenYouTube(context, uiState)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            val action = RecipeDetailsAction.NavigateUp
                            onAction(action)
                        }
                    ) {
                        ArrowBackIcon()
                    }
                },
                title = {
                    val title = when (uiState) {
                        is RecipeDetailsUiState.IsLoading -> ""
                        is RecipeDetailsUiState.HasData -> uiState.recipe.title
                    }
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            openDeleteDialog = true
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_youtube),
                            contentDescription = "YouTube icon"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Divider()
            if (uiState.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            Crossfade(
                targetState = uiState.isLoading,
                label = ""
            ) { isLoading ->
                if (!isLoading) {
                    when (uiState) {
                        is RecipeDetailsUiState.IsLoading -> {

                        }

                        is RecipeDetailsUiState.HasData -> {
                            Column {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(bottom = 8.dp)
                                ) {
                                    item {
                                        Column(
                                            modifier = Modifier.padding(16.dp),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = uiState.recipe.title,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            Text(
                                                text = uiState.recipe.author,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                    items(uiState.recipe.steps) { stepUiState ->
                                        BrewingStepListItem(brewingStepUiState = stepUiState)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun onOpenYouTube(context: Context, uiState: RecipeDetailsUiState) {
    if (uiState !is RecipeDetailsUiState.HasData) return
    val youtubeId = uiState.recipe.youtubeId
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("https://www.youtube.com/watch?v=${youtubeId}}")
    startActivity(context, intent, null)
}