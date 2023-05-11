package com.coffee.mycoffeeassistant.ui.screens.recipedetails

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coffee.mycoffeeassistant.ui.AppViewModelProvider

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RecipeDetailsScreen(
    recipeDetailsViewModel: RecipeDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
//    TODO: Change background color for iframe in dark mode

    val videoId = "tltBHjmIUJ0"
    val iframeHtml = "<html>" +
            "<meta name=\"viewport\" content=\"width=640\">" +
            "<body style=\"margin: 0px; padding: 0px\">" +
            "<iframe " +
            "id=\"player\" " +
            "frameborder=\"0\" " +
            "allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" " +
            "width=\"640\" " +
            "height=\"360\" " +
            "src=\"https://www.youtube.com/embed/${videoId}?version=3&amp;enablejsapi=1&amp;controls=1&amp;fs=0\">" +
            "</iframe>" +
            "</body>" +
            "</html>"

    Column() {
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

                        webViewClient = IFrameWebViewClient(it)
                        loadData(iframeHtml, "text/html", "utf-8")
                    }
                },
                update = {
                }
            )
        }
    }
}

class IFrameWebViewClient(private val context: Context) : WebViewClient() {
    @Override
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(request?.url.toString()))
        context.startActivity(intent)
        return true
    }
}

