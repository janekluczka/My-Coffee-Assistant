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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.coffee.mycoffeeassistant.ui.components.BrewingStepListItem
import com.coffee.mycoffeeassistant.ui.model.components.RecipeDetailsUiState
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RecipeDetailsScreen(
    recipeDetailsUiState: RecipeDetailsUiState,
    navigateUp: () -> Unit,
) {
    val context = LocalContext.current

    // TODO: https://github.com/google/accompanist/pull/1557

    val webViewState = rememberWebViewState(url = recipeDetailsUiState.iframeHtml)

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { navigateUp() },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    )
                },
                title = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                        loadData(recipeDetailsUiState.iframeHtml, "text/html", "utf-8")
                    }
                }
            )
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