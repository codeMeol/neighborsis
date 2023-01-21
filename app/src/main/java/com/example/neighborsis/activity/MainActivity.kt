package com.example.neighborsis.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.neighborsis.FCMMessagingService
import com.example.neighborsis.R
import com.example.neighborsis.WebView.WebviewInterface
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
    val fcm = FCMMessagingService()

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


        val extras = intent.extras
        var intentLinkURL =""
        intentLinkURL = if(extras?.getString("linkURL")==null) {
            "file:///android_asset/new.html"
//            "https://dunni.co.kr/"
        } else {
            extras.getString("linkURL")
        }!!
        var mSettingAdapter = SettingAdapter(getSettingModelList())
        val mItemClickListener = OnItemClickListener { parent, view, position, l_position ->
            // parent는 AdapterView의 속성의 모두 사용 할 수 있다.
            val tv = parent.adapter.getItemId(position).toString()
            if (tv.equals("2")) {
                val intent = Intent(applicationContext, AdminPushForFirebaseActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                this.startActivity(intent)
            }
            Toast.makeText(applicationContext, tv, Toast.LENGTH_SHORT).show()
        }

        val mSettingList = binding.adminPushLayout.findViewById<ListView>(R.id.item_list)
        mSettingList.adapter = mSettingAdapter
        mSettingList.onItemClickListener = mItemClickListener

        MobileAds.initialize(this)
            Log.d("준영테스트","${intentLinkURL}")
        verifyStoragePermissions(this)
        shouldOverrideUrlLoading(webView,intentLinkURL!!)
        val webviewInterface=WebviewInterface(this)
        webView!!.addJavascriptInterface(webviewInterface,"Native")

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

        Log.d("준영테스트", "${resultCode}= resultCode , ${requestCode} = requestCode , ${data} = data")

        val message = data?.getStringExtra("linkUrl").toString()

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

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
    fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
      Log.d("shouldOverrideUrl ","안녕$url")
        try {
            /**
             * 201229
             * 카카오링크 오류 수정을 위해 아래 if문을 추가함.
             */
            if (url != null && url.startsWith("intent:kakaolink:")) {
                try {
                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    val existPackage =
                        packageManager.getLaunchIntentForPackage(intent.getPackage()!!)
                    if (existPackage != null) {
                        startActivity(intent)
                    } else {
                        val marketIntent = Intent(Intent.ACTION_VIEW)
                        marketIntent.data = Uri.parse("market://details?id=" + intent.getPackage())
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
        webView!!.loadUrl(url!!)
        return false
    }


    fun verifyStoragePermissions(activity: Activity?) {
        val REQUEST_EXTERNAL_STORAGE = 1
        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        // Check if we have write permission
        val permission =
            ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                  REQUEST_EXTERNAL_STORAGE
            )
        }
    }
}
