package com.example.neighborsis

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.neighborsis.adapter.SettingAdapter
import com.example.neighborsis.databinding.ActivityMainBinding
import com.example.neighborsis.databinding.AdminPushLayoutBinding
import com.example.neighborsis.databinding.SettingItemBinding
import com.example.neighborsis.dataclass.SettingModel
import com.example.neighborsis.util.PopupDialog
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity() {
    var webView: WebView? = null
    var webViewBtn: ImageView? = null
    var settingBtn: ImageView? = null
    var mViewFlipper: ViewFlipper? = null
    val SettingItem = SettingItemActivity()

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
        var mSettingAdapter : SettingAdapter = SettingAdapter()
        var mView: AdminPushLayoutBinding = binding.adminPushLayout
        var mItemList = mView.itemList
        mItemList.adapter= SettingAdapter()
        mSettingAdapter.submitList()


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
    private fun getProductItemList(): ArrayList<SettingModel> {
        var resultList = arrayListOf<SettingModel>()

        var cnt = 0
        //
        while (cnt++ < 3) {
            val id :ImageView =SettingItem.findViewById(R.id.)
            val thumbnail : TextView =
            val title : TextView =
            val price : ImageView =

            val settingItem = SettingModel(id, thumbnail!!, title, price)
            resultList.add(settingItem)
        }
        return resultList
    }
}