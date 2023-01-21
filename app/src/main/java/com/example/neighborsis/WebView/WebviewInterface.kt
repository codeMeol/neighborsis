package com.example.neighborsis.WebView

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast

class WebviewInterface (private val mContext: Context) {

    @JavascriptInterface
    fun showMessage() {
        Log.d("준영테스트","webview 통신 성공 ")
    }


}
