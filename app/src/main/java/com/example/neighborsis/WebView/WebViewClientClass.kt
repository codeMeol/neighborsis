package com.example.neighborsis.WebView

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient


class WebViewClientClass(context: Context) : WebViewClient() {
    val context = context
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        return if (url.startsWith(INTENT_PROTOCOL_START)) {
            val customUrlStartIndex = INTENT_PROTOCOL_START.length
            val customUrlEndIndex = url.indexOf(INTENT_PROTOCOL_INTENT)

            if (customUrlEndIndex < 0) {
                false
            } else {
                val customUrl = url.substring(customUrlStartIndex, customUrlEndIndex)
                Log.d("준영테스트","커스텀 url = $customUrl")
                try {
                   val intent : Intent? = context.packageManager.getLaunchIntentForPackage("com.kakao.talk")
                    intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    val packageStartIndex = customUrlEndIndex + INTENT_PROTOCOL_INTENT.length
                    val packageEndIndex = url.indexOf(INTENT_PROTOCOL_END)
                    val packageName = "com.kakao.talk"

                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW, Uri.parse(
                                GOOGLE_PLAY_STORE_PREFIX + packageName
                            )
                        )
                    )
                }
                view.loadUrl(customUrl)
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