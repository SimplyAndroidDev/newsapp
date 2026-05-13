package com.android.newsapp.webview.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.newsapp.webview.presentation.ui.WebViewUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebviewViewModel @Inject constructor() : ViewModel() {
    private val _uiEvent = MutableSharedFlow<WebViewUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onShareClicked(url: String) {
        viewModelScope.launch {
            try {
                val formattedMessage = "Check out this breaking news: $url"
                _uiEvent.emit(WebViewUiEvent.Share(formattedMessage))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onOpenInBrowser(url: String) {
        viewModelScope.launch {
            try {
                _uiEvent.emit(WebViewUiEvent.OpenInBrowser(url))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}