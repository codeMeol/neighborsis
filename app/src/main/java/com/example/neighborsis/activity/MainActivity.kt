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
import android.webkit.ValueCallback
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
import com.google.android.gms.ads.nativead.NativeAdOptions


class MainActivity : AppCompatActivity() {
    var webView: WebView? = null
    var webViewBtn: ImageView? = null
    var settingBtn: ImageView? = null
    var mViewFlipper: ViewFlipper? = null
    private var sharedPref: Sharedpref? = null
    var isAdmin: Boolean = false
    var userId = ""
    var mSettingList: ListView? = null
    private var CancelPopUp: PopupDialog? = null
    lateinit var adLoader: AdLoader

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        webView = binding.webView
        webViewBtn = binding.webViewBtn
        settingBtn = binding.settingBtn
        mViewFlipper = binding.viewFlipper
        val settingBackBtn: ImageView
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
        CancelPopUp = PopupDialog(this, finishApp = { finish() })
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
        settingBackBtn = binding.adminPushLayout.findViewById<ImageView>(R.id.setting_back_btn)
        settingBackBtn.setOnClickListener { it ->
            if (FLIPPERCOUNT == 1) {
                FLIPPERCOUNT -= 1
                mViewFlipper?.showPrevious()
            }
        }
        mSettingList = binding.adminPushLayout.findViewById<ListView>(R.id.item_list)
        mSettingList!!.adapter = mSettingAdapter
        mSettingList!!.onItemClickListener = mItemClickListener

        MobileAds.initialize(this)
        adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { it ->
                if (adLoader.isLoading) {
                    //처음에 아마 생성 해주긴 했는데 혹시 몰라서 null 체크
                    if (CancelPopUp?.binding != null) {
                        CancelPopUp!!.binding!!.myTemplate.setNativeAd(it)
                    } else {
                    }
                } else {
                }
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the failure by logging, altering the UI, and so on.
                }
            }).withNativeAdOptions(
                NativeAdOptions.Builder()
                    // Methods in the NativeAdOptions.Builder class can be
                    // used here to specify individual options settings.
                    .build()
            ).build()

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

            override fun onScrollChange(
                view: View?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY > oldScrollY) {
                    scrollDown()
                } else if (scrollY < oldScrollY) {
                    scrollUp()
                }
            }
        }
        webView!!.setOnScrollChangeListener(webViewScrollListener)
        webView!!.loadUrl(intentLinkURL)
        webView!!.addJavascriptInterface(webviewInterface, "Native")

        webViewBtn?.setOnClickListener {
            if (FLIPPERCOUNT == 1) {
                FLIPPERCOUNT -= 1
                mViewFlipper?.showPrevious()
            }
        }

        settingBtn?.setOnClickListener {
            if (FLIPPERCOUNT == 0) {
                FLIPPERCOUNT += 1
                mViewFlipper?.showNext()
                webView!!.evaluateJavascript(
                    "document.querySelector('.xans-member-var-name').innerText"
                ) {
                    // value will contain the text content of the element
                    val t = if (!it.equals("null")) it.toString() else "로그인이 필요합니다."
                    userId = t
                    mSettingList!!.adapter = SettingAdapter(getSettingModelList(isAdmin, userId))
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        mSettingList!!.adapter = SettingAdapter(getSettingModelList(isAdmin, userId))
    }

    override fun onBackPressed() {
        if (webView?.canGoBack()!!) {
            webView?.goBack()
        } else {
            adLoader.loadAd(AdRequest.Builder().build())
            CancelPopUp!!.show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


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
                    if (sharedPref!!.getPrefBool(PushConstants.PUSH_SUBSCRIBED_MARKETING)) "동의"
                    else "미동의"
                modeDrawable = getDrawable(R.drawable.sending_mail)
                modeExStr = "푸시알림 동의 정보"
                value = "푸시 $pushInfo\n이벤트알림 $marketingInfo"

            } else if (cnt == 1) {

            }

            var rightBtn = getDrawable(R.drawable.right_btn)

            val settingItem = SettingModel(modeDrawable!!, modeExStr, value, rightBtn!!)
            resultList.add(settingItem)

        }
        return resultList
    }

}
