package com.android.newsapp.webview.presentation.ui

sealed class WebViewUiEvent {
    data class Share(val text: String): WebViewUiEvent()
    data class OpenInBrowser(val url: String): WebViewUiEvent()
}