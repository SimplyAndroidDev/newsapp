package com.android.newsapp.webview.presentation.ui

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.newsapp.core.common.Utility.shareText
import com.android.newsapp.uicomponents.ExpandableFabMenu
import com.android.newsapp.webview.presentation.viewmodel.WebviewViewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(url: String) {
    val viewModel: WebviewViewModel = hiltViewModel()
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    var isLoading by remember { mutableStateOf(true) }
    val webView = remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            // Optimize settings
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                cacheMode = WebSettings.LOAD_DEFAULT
            }

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    isLoading = false
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (newProgress >= 90) isLoading = false
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            webView.stopLoading()
            webView.destroy()
        }
    }

    LaunchedEffect(url) {
        webView.loadUrl(url)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                is WebViewUiEvent.Share -> {
                    context.shareText(it.text)
                }

                is WebViewUiEvent.OpenInBrowser -> {
                    uriHandler.openUri(it.url)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { webView },
            modifier = Modifier.fillMaxSize()
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        ExpandableFabMenu(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(bottom = 32.dp),
            onShareClick = {
                viewModel.onShareClicked(url)
            },
            openInBrowser = {
                viewModel.onOpenInBrowser(url)
            }
        )
    }
}