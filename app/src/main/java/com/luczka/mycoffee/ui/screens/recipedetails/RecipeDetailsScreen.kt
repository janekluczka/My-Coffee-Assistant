package com.luczka.mycoffee.ui.screens.recipedetails

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
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
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.buttons.BackIconButton
import com.luczka.mycoffee.ui.components.listitem.BrewingStepListItem
import com.luczka.mycoffee.ui.models.RecipeUiState

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
                    BackIconButton(
                        onClick = {
                            val action = RecipeDetailsAction.NavigateUp
                            onAction(action)
                        }
                    )
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
                                        BrewingStepListItem(stepUiState = stepUiState)
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

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YouTubePlayer(
    recipeUiState: RecipeUiState
) {
    // TODO: https://github.com/google/accompanist/pull/1557
    // TODO: Change background color for iframe in dark mode if possible
    /**
     * iframe parameters
     *
     * version: player version
     * enablejsapi:
     *  0 - JavaScript disabled
     *  1 - JavaScript enabled
     * controls:
     *  0 - controls hidden,
     *  1 - controls visible & Flash loaded immidiately
     *  2 - controls visible & Flash loaded when video starts playing
     * fs:
     *  0 - fullscreen button hidden
     *  1 - fullscreen button visible
     *
     * more at: https://developers.google.com/youtube/player_parameters?hl=pl
     */

    val context = LocalContext.current

    val iframeWidth = 640
    val iframeHeight = 360
    val iframeHtml = "<html>" +
            "<meta name=\"viewport\" content=\"width=${iframeWidth}\">" +
            "<style>iframe { overflow:hidden; }</style>" +
            "<body style=\"margin: 0px; padding: 0px\">" +
            "<iframe " +
            "id=\"player\" " +
            "frameborder=\"0\" " +
            "allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" " +
            "width=\"${iframeWidth}\" " +
            "height=\"${iframeHeight}\" " +
            "src=\"https://www.youtube.com/embed/${recipeUiState.youtubeId}?version=3&amp;enablejsapi=1&amp;controls=1&amp;fs=0\">" +
            "</iframe>" +
            "</body>" +
            "</html>"

    val webViewState = rememberWebViewState(url = iframeHtml)

    WebView(
        modifier = Modifier
            .aspectRatio(16f / 9f)
            .fillMaxWidth(),
        state = webViewState,
        client = IFrameAccompanistWebViewClient(context = context),
        onCreated = { webView ->
            webView.apply {
                settings.apply {
                    javaScriptEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                }
                setLayerType(ViewGroup.LAYER_TYPE_HARDWARE, null)
                webViewClient = IFrameWebViewClient(context)
                loadData(iframeHtml, "text/html", "utf-8")
            }
        }
    )
}

fun onOpenYouTube(context: Context, uiState: RecipeDetailsUiState) {
    if (uiState !is RecipeDetailsUiState.HasData) return
    val youtubeId = uiState.recipe.youtubeId
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("https://www.youtube.com/watch?v=${youtubeId}}")
    startActivity(context, intent, null)
}


// TODO: Handle WebView errors
class IFrameWebViewClient(private val context: Context) : WebViewClient() {
    @Override
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val uri = Uri.parse(request?.url.toString())
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
        return true
    }
}

class IFrameAccompanistWebViewClient(private val context: Context) : AccompanistWebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val uri = Uri.parse(request?.url.toString())
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
        return true
    }
}