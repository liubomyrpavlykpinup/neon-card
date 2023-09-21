package com.tocaboca.tocacar.presentation.core

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class NeonCardSettingsWrapper {

    fun getInstance(): NeonCardSettings = NeonCardSettings()
}

class NeonCardSettings {

    @SuppressLint("SetJavaScriptEnabled")
    fun configure(game: WebView): WebView {
        return game.also {
            with(it) {
                settings.userAgentString.replace(" wv ", "")
                settings.allowContentAccess = true
                settings.allowFileAccess = true
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.javaScriptCanOpenWindowsAutomatically = true
                settings.allowFileAccess = true
                settings.allowContentAccess = true
                settings.loadWithOverviewMode = true
            }
        }
    }
}

fun WebView.additionalSetup(view: WebViewClient, client: WebChromeClient, name: String): WebView {
    return this.also {
        with(it) {
            webViewClient = view
            webChromeClient = client

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

            loadUrl(name)
        }
    }
}