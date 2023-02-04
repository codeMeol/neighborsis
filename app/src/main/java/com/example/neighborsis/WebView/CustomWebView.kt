package com.example.neighborsis.WebView

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView


class CustomWebView : WebView {
    private var oldY: Float = 0.toFloat()
    private var webViewScrollListener: WebViewScrollListener? = null

    interface WebViewScrollListener {
        fun scrollDown()
        fun scrollUp()
    }

    fun setWebViewScrollListener(webViewScrollListener: WebViewScrollListener) {
        this.webViewScrollListener = webViewScrollListener
    }

    constructor(context: Context) : super(context) {
        // TODO Auto-generated constructor stub
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        // TODO Auto-generated constructor stub
    }


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        // TODO Auto-generated constructor stub
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                oldY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                if (event.y > oldY) {
                    webViewScrollListener?.scrollDown()
                }

                if (event.y < oldY) {
                    webViewScrollListener?.scrollUp()

                }
            }
            else -> {
            }
        }
        return super.onTouchEvent(event)
    }

}