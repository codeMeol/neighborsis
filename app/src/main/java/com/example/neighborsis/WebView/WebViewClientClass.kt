package com.example.neighborsis.WebView

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.startActivity


class WebViewClientClass : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

        try {
            /**
             * 201229
             * 카카오링크 오류 수정을 위해 아래 if문을 추가함.
             */
            if (url != null && url.startsWith("intent:kakaolink:")) {
                try {
                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    val existPackage: Intent =
                        Activity().packageManager.getLaunchIntentForPackage(intent.getPackage()!!)!!
                    if (existPackage != null) {
                        startActivity(existPackage.get)
                    } else {
                        val marketIntent = Intent(Intent.ACTION_VIEW)
                        marketIntent.data =     Uri.parse("market://details?id=" + intent.getPackage())
                        startActivity(marketIntent)
                    }
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return false
    }
}
