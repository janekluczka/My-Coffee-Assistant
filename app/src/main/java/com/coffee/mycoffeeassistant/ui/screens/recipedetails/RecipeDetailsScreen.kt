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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RecipeDetailsScreen() {
    val videoId = "tltBHjmIUJ0"

    val filePath = "file:///android_asset/iframeplayer.html"

    Box(
        modifier = Modifier.aspectRatio(16f / 9f)
    ) {
        AndroidView(
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    settings.apply {
                        javaScriptEnabled = true
//                        loadWithOverviewMode = true
//                        useWideViewPort = true
                    }
                    webViewClient = IFrameWebViewClient(it)
                    loadUrl(filePath)
                }
            },
            update = {
//                it.loadUrl(filePath)
            }
        )
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

