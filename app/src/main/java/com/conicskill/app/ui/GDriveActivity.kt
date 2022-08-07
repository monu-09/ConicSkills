package com.conicskill.app.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK
import android.webkit.WebView
import butterknife.BindView
import com.conicskill.app.R
import com.conicskill.app.base.BaseActivity

class GDriveActivity : BaseActivity() {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.webViewGDrive)
    lateinit var webViewGDrive: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webViewGDrive.webViewClient = Browser()
        webViewGDrive.loadUrl(intent.getStringExtra("url")!!)
        webViewGDrive.settings.javaScriptEnabled = true
        webViewGDrive.settings.allowContentAccess = true
        webViewGDrive.settings.cacheMode = LOAD_CACHE_ELSE_NETWORK
    }

    override fun layoutRes(): Int {
        return R.layout.activity_gdrive
    }

    @Suppress("DEPRECATION", "OverridingDeprecatedMember")
    class Browser : android.webkit.WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            url: String?
        ): Boolean {
            view!!.loadUrl(url!!)
            return true
        }
    }

    override fun onBackPressed() {
        if(webViewGDrive.canGoBack()) {
            webViewGDrive.goBack()
        } else {
            super.onBackPressed()
        }
    }
}