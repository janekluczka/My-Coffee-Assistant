package com.coffee.mycoffeeassistant.ui.screens.recipedetails

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coffee.mycoffeeassistant.ui.AppViewModelProvider
import com.coffee.mycoffeeassistant.ui.components.BrewingStepCard

@Suppress("SpellCheckingInspection")
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RecipeDetailsScreen(
    youtubeId: String,
    viewModel: RecipeDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val recipeDetailsUiState = viewModel.recipeDetailsUiState
    val iframeWidth = 640
    val iframeHeight = 640 * 9 / 16

    // TODO: Change background color for iframe in dark mode if possible
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
            "src=\"https://www.youtube.com/embed/${youtubeId}?version=3&amp;enablejsapi=1&amp;controls=1&amp;fs=0\">" +
            "</iframe>" +
            "</body>" +
            "</html>"

    viewModel.getRecipe(youtubeId)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.aspectRatio(16f / 9f)) {
            AndroidView(
                factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        settings.apply {
                            javaScriptEnabled = true
                            loadWithOverviewMode = true
                            useWideViewPort = true
                        }
                        setLayerType(ViewGroup.LAYER_TYPE_HARDWARE, null)
                        webViewClient = IFrameWebViewClient(it)
                        loadData(iframeHtml, "text/html", "utf-8")
                    }
                },
                update = {

                }
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recipeDetailsUiState.steps.size) { index ->
                BrewingStepCard(
                    number = (index + 1).toString(),
                    description = recipeDetailsUiState.steps[index]["description"].toString(),
                    time = recipeDetailsUiState.steps[index]["time"].toString()
                )
            }
        }
    }
}

// TODO: Handle WebView errors
class IFrameWebViewClient(private val context: Context) : WebViewClient() {
    @Override
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(request?.url.toString()))
        context.startActivity(intent)
        return true
    }
}

