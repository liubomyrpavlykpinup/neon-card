package com.tocaboca.tocacar.presentation.core

import android.annotation.SuppressLint
import android.content.Context
import android.os.Message
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class ClientWrapper(
    private val context: Context
) {

    private inner class LocalClient : WebChromeClient() {

        @SuppressLint("SetJavaScriptEnabled")
        private fun configure(view: WebView) {
            view.apply {
                webChromeClient = this@LocalClient
                settings.domStorageEnabled = true
                settings.javaScriptCanOpenWindowsAutomatically = true
                settings.javaScriptEnabled = true
                settings.setSupportMultipleWindows(true)
            }
        }

        override fun onCreateWindow(
            view: WebView,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message
        ): Boolean {
            val myView = WebView(context)
            configure(myView)

            val transport = resultMsg.obj as WebView.WebViewTransport
            transport.webView = myView
            resultMsg.sendToTarget()
            myView.webViewClient = object : WebViewClient() {
                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    param: String,
                ): Boolean {
                    view.loadUrl(param)
                    return true
                }
            }
            return true
        }
    }

    companion object {
        fun getInstance(context: Context): WebChromeClient {
            return ClientWrapper(context).LocalClient()
        }

    }
}