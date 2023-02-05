package com.example.neighborsis.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnScrollChangeListener
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.neighborsis.R
import com.example.neighborsis.SharedPreferenceClass.Sharedpref
import com.example.neighborsis.WebView.CustomWebView
import com.example.neighborsis.WebView.WebViewClientClass
import com.example.neighborsis.WebView.WebviewInterface
import com.example.neighborsis.adapter.SettingAdapter
import com.example.neighborsis.databinding.ActivityMainBinding
import com.example.neighborsis.dataclass.SettingModel
import com.example.neighborsis.retrofit2.Constants.PushConstants
import com.example.neighborsis.util.PopupDialog
import com.example.neighborsis.util.PushCheckDialog
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions


class MainActivity : AppCompatActivity() {
    var webView: WebView? = null
    var webViewBtn: ImageView? = null
    var settingBtn: ImageView? = null
    var mViewFlipper: ViewFlipper? = null
    private var sharedPref: Sharedpref? = null
    var isAdmin: Boolean = false
    var mSettingList :ListView? =null
    lateinit var  adLoader :AdLoader
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        webView = binding.webView
        webViewBtn = binding.webViewBtn
        settingBtn = binding.settingBtn
        mViewFlipper = binding.viewFlipper
        val pushDialog = PushCheckDialog()
        var FLIPPERCOUNT = 0
        val extras = intent.extras
        var intentLinkURL = ""
        var mSettingAdapter: SettingAdapter?
        val webviewclass = WebViewClientClass(this)
        val webviewInterface = WebviewInterface(this)
        webView?.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }

        sharedPref = Sharedpref(this.getString(R.string.pushOkLevel), this)
        Log.d(
            "준영테스트",
            "sharedPref = ${sharedPref!!.getPrefBool(PushConstants.PUSH_SUBSCRIBED_INITIALIZING)}"
        )
        if (sharedPref!!.getPrefBool(PushConstants.PUSH_SUBSCRIBED_INITIALIZING)) {

        } else {
            pushDialog.show(
                supportFragmentManager, "pushDialog"
            )
        }




        intentLinkURL = if (extras?.getString("linkURL") == null) {
            "https://dunni.co.kr/"
        } else {
            extras.getString("linkURL")
        }!!
        mSettingAdapter = SettingAdapter(getSettingModelList(isAdmin, "userId"))
        val mItemClickListener = OnItemClickListener { parent, view, position, l_position ->
            // parent는 AdapterView의 속성의 모두 사용 할 수 있다.
            val tv = parent.adapter.getItemId(position).toString()
            if (tv.equals("2")) {
                val intent = Intent(applicationContext, AdminPushForFirebaseActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                this.startActivity(intent)
            } else if (tv.equals("1")) {
                val intent = Intent(applicationContext, testActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                this.startActivity(intent)
            }
        }

        mSettingList = binding.adminPushLayout.findViewById<ListView>(R.id.item_list)
        mSettingList!!.adapter = mSettingAdapter
        mSettingList!!.onItemClickListener = mItemClickListener

        MobileAds.initialize(this)
        adLoader =  AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd {
                if (adLoader.isLoading) {

                } else {

                }
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the failure by logging, altering the UI, and so on.
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                // Methods in the NativeAdOptions.Builder class can be
                // used here to specify individual options settings.
                .build()).build()

        Log.d("준영테스트", "${intentLinkURL}")
        adLoader.loadAd(AdRequest.Builder().build())
        verifyStoragePermissions(this)

        webView!!.webViewClient = webviewclass
        val webViewScrollListener = object : CustomWebView.WebViewScrollListener,
            OnScrollChangeListener {
            var params = binding.viewFlipper.layoutParams as LinearLayout.LayoutParams
            override fun scrollDown() {
                params.weight = 10f
                binding.bottomBtnLayout.visibility = View.GONE
            }

            override fun scrollUp() {
                params.weight = 9.2f
                binding.bottomBtnLayout.visibility = View.VISIBLE
            }

            override fun onScrollChange(view: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                Log.d("준영테스트","$scrollX  $scrollY   $oldScrollX   $oldScrollY")
                if(scrollY>oldScrollY){
                    scrollDown()
                }
                else if(scrollY<oldScrollY){
                    scrollUp()
                }
            }
        }
        webView!!.setOnScrollChangeListener(webViewScrollListener)
        webView!!.loadUrl(intentLinkURL)
        webView!!.addJavascriptInterface(webviewInterface, "Native")

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
        mSettingList!!.adapter = SettingAdapter( getSettingModelList(isAdmin,"userId"))
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

    private fun getSettingModelList(isAdmin: Boolean, userId: String): ArrayList<SettingModel> {
        var resultList = arrayListOf<SettingModel>()
        var modeDrawable = getDrawable(R.drawable.setting_user_icon)
        var modeExStr = "사용자 정보"
        var value: String = "$userId"
        var cnt = 0
        var itemSize = if (!isAdmin) 2 else 3
        while (cnt++ < itemSize) {
            if (cnt == 3) {
                modeDrawable = getDrawable(R.drawable.setting_user_icon)
                modeExStr = "개발자모드"
                value = "개발자 준영몬"

            } else if (cnt == 2) {
                var pushInfo: String =
                    if (sharedPref!!.getPrefBool(PushConstants.PUSH_SUBSCRIBED_SYSTEM)) "동의"
                    else "미동의"
                var marketingInfo: String =
                    if(sharedPref!!.getPrefBool(PushConstants.PUSH_SUBSCRIBED_MARKETING)) "동의"
                    else "미동의"
                modeDrawable = getDrawable(R.drawable.sending_mail)
                modeExStr = "푸시알림 동의 정보"
                value = "푸시 $pushInfo\n이벤트알림 $marketingInfo"

            }

            var rightBtn = getDrawable(R.drawable.right_btn)

            val settingItem = SettingModel(modeDrawable!!, modeExStr, value, rightBtn!!)
            resultList.add(settingItem)

        }
        Log.d("준영테스트", "cnt = $cnt, result =$resultList")
        return resultList
    }


    fun verifyStoragePermissions(activity: Activity?) {
        val REQUEST_EXTERNAL_STORAGE = 1
        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        // Check if we have write permission
        val permission =
            ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
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
