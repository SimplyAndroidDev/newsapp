package com.android.newsapp.core.common

import android.content.Context
import android.content.Intent

object Utility {
    fun Context.shareText(text: String, title: String = "Share via") {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, title)
        startActivity(shareIntent)
    }
}