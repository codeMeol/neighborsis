package com.example.neighborsis.WebView

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient


class WebViewClientClass(context: Context) : WebViewClient() {
    val context = context
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        return if (url.startsWith(INTENT_PROTOCOL_START)) {
            val customUrlEndIndex = url.indexOf(INTENT_PROTOCOL_INTENT)

            if (customUrlEndIndex < 0) {
                false
            } else {
                try {
                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    val existPackage: Intent? =
                        intent.getPackage()
                            ?.let { context.packageManager.getLaunchIntentForPackage(it) }
                    if (existPackage != null) {
                        context.startActivity(intent)
                    }
                } catch (e: ActivityNotFoundException) {

                    val packageName = "com.kakao.talk"

                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW, Uri.parse(
                                GOOGLE_PLAY_STORE_PREFIX + packageName
                            )
                        )
                    )
                }

                true
            }
        } else {
            false
        }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)


    }

    companion object {
        const val INTENT_PROTOCOL_START = "intent:"
        const val INTENT_PROTOCOL_INTENT = "#Intent;"
        const val INTENT_PROTOCOL_END = ";end;"
        const val GOOGLE_PLAY_STORE_PREFIX = "market://details?id="
    }
}