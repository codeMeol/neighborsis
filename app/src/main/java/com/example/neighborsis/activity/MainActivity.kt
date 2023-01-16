package com.example.neighborsis.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.example.neighborsis.FCMMessagingService
import com.example.neighborsis.R
import com.example.neighborsis.adapter.SettingAdapter
import com.example.neighborsis.databinding.ActivityMainBinding
import com.example.neighborsis.dataclass.SettingModel
import com.example.neighborsis.util.PopupDialog
import com.google.android.gms.ads.MobileAds



@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    var webView: WebView? = null
    var webViewBtn: ImageView? = null
    var settingBtn: ImageView? = null
    var mViewFlipper: ViewFlipper? = null
    var fcm = FCMMessagingService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        webView = binding.webView
        webViewBtn = binding.webViewBtn
        settingBtn = binding.settingBtn
        mViewFlipper = binding.viewFlipper
        var FLIPPERCOUNT = 0
        webView?.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }
        var mSettingAdapter = SettingAdapter(getSettingModelList())
        val mItemClickListener = OnItemClickListener { parent, view, position, l_position ->
            // parent는 AdapterView의 속성의 모두 사용 할 수 있다.
            val tv = parent.adapter.getItemId(position).toString()
            if(tv.equals("2")){
                val intent =Intent(applicationContext, AdminPushForFirebaseActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                    this.startActivity(intent)
            }
            Toast.makeText(applicationContext, tv, Toast.LENGTH_SHORT).show()
        }

        val mSettingList = binding.adminPushLayout.findViewById<ListView>(R.id.item_list)
        mSettingList.adapter = mSettingAdapter
        mSettingList.onItemClickListener = mItemClickListener




        MobileAds.initialize(this)
        webView?.loadUrl("https://dunni.co.kr/")

        webViewBtn?.setOnClickListener { it ->
            if (FLIPPERCOUNT == 1) {
                FLIPPERCOUNT -= 1
                mViewFlipper?.showPrevious()
            }
        }
        settingBtn?.setOnClickListener { it ->
            if (FLIPPERCOUNT == 0) {
                FLIPPERCOUNT += 1
                mViewFlipper?.showNext()
            }
        }

    }

    override fun onResume() {
        super.onResume()
       var intentLinkURL = Intent().extras?.getString("linkURL")
        if(fcm.url!=null){
            Log.d("준영테스트","${fcm.url}")
        }
        Log.d("onResume","${intentLinkURL}")
    }
    override fun onBackPressed() {
        if (webView?.canGoBack()!!) {
            webView?.goBack()
        } else {
            var CancelPopUp: PopupDialog = PopupDialog(this, finishApp = { finish() })
            CancelPopUp.show()

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

           Log.d("준영테스트","${resultCode}= resultCode , ${requestCode} = requestCode , ${data} = data")




           val message = data?.getStringExtra("linkUrl").toString()

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        }
//
//        if (message.equals("꺼주세요")) {
//            finish()
//        } else if (message.equals("돌아갔음")) {
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//        }


    private fun getSettingModelList(): ArrayList<SettingModel> {
        var resultList = arrayListOf<SettingModel>()
        var thumbnail: String
        var cnt = 0
        while (cnt++ < 3) {
            var id = getDrawable(R.drawable.setting_user_icon)
            if (cnt == 2) {
                thumbnail = "개발자모드"
            } else {
                thumbnail = "사용자 정보"
            }
            var title: String = "adminA123"
            var price = getDrawable(R.drawable.right_btn)

            val settingItem = SettingModel(id!!, thumbnail, title, price!!)
            resultList.add(settingItem)
            Log.d("준영테스트", "$resultList")
        }
        return resultList
    }
}