package com.example.neighborsis

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.ViewFlipper
import com.example.neighborsis.databinding.ActivityMainBinding
import com.example.neighborsis.util.PopupDialog
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {
    var webView: WebView? = null
    var webViewBtn: ImageView? = null
    var settingBtn: ImageView? = null
    var mViewFlipper: ViewFlipper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        webView = binding.webView
        webViewBtn = binding.webViewBtn
        settingBtn = binding.settingBtn
        mViewFlipper = binding.viewFlipper
        webView?.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }

        var fcm = FCMMessagingService()

        MobileAds.initialize(this)
        webView?.loadUrl("https://dunni.co.kr/")

        webViewBtn?.setOnClickListener { it ->
            mViewFlipper?.showNext()
        }
        settingBtn?.setOnClickListener { it ->
            mViewFlipper?.showPrevious()
        }

    }

    override fun onBackPressed() {
        if (webView?.canGoBack()!!) {
            webView?.goBack()
        } else {
            var CancelPopUp: PopupDialog = PopupDialog(this, finishApp = { finish() })
            CancelPopUp.show()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        lateinit var message: String
        Toast.makeText(
            this,
            "${message} = message ${requestCode} = resultcode ${data} = data",
            Toast.LENGTH_SHORT
        ).show()
        if (requestCode == Activity.RESULT_OK) {
            message = data?.getStringExtra("key1").toString()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        if (message.equals("꺼주세요")) {
            finish()
        } else if (message.equals("돌아갔음")) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}