package com.example.taipeizookotlin

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {

    private lateinit var mUrlString: String
    private lateinit var mWebView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview_activity)
        mWebView = findViewById(R.id.mWebView)
        getBundle()
        mWebView.run {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(mUrlString)
        }
    }

    private fun getBundle() {
        val iBundle = intent.extras
        mUrlString = iBundle!!.getString("getUrl").toString()
    }
}