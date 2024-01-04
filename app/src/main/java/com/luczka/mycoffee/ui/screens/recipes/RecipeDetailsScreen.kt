package com.luczka.mycoffee.ui.screens.recipes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewState
import com.google.accompanist.web.rememberWebViewState
import com.luczka.mycoffee.ui.components.BackIconButton
import com.luczka.mycoffee.ui.components.BrewingStepListItem
import com.luczka.mycoffee.ui.model.RecipeDetailsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    widthSizeClass: WindowWidthSizeClass,
    recipeDetailsUiState: RecipeDetailsUiState?,
    navigateUp: () -> Unit,
) {
    if(recipeDetailsUiState == null) return

    key(recipeDetailsUiState.youtubeId) {
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
                "src=\"https://www.youtube.com/embed/${recipeDetailsUiState.youtubeId}?version=3&amp;enablejsapi=1&amp;controls=1&amp;fs=0\">" +
                "</iframe>" +
                "</body>" +
                "</html>"

        // TODO: Refresh video on new url
        val webViewState = rememberWebViewState(url = iframeHtml)

        when (widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            navigationIcon = { BackIconButton(onClick = navigateUp) },
                            title = {}
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        YouTubePlayer(webViewState, iframeHtml)
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
                                        text = recipeDetailsUiState.title,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = recipeDetailsUiState.author,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                            items(recipeDetailsUiState.steps) { stepUiState ->
                                BrewingStepListItem(stepUiState = stepUiState)
                            }
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    item {
                        Column {
                            YouTubePlayer(webViewState, iframeHtml)
                            Surface {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = recipeDetailsUiState.title,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = recipeDetailsUiState.author,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                            recipeDetailsUiState.steps.forEach { stepUiState ->
                                BrewingStepListItem(stepUiState = stepUiState)
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
private fun YouTubePlayer(
    webViewState: WebViewState,
    iframeHtml: String
) {
    val context = LocalContext.current
    WebView(
        modifier = Modifier.aspectRatio(16f / 9f),
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