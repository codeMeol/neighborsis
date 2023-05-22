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
    private var webView: WebView? = null
    private var webViewBtn: ImageView? = null
    private var settingBtn: ImageView? = null
    private var mViewFlipper: ViewFlipper? = null
    private var sharedPref: Sharedpref? = null
    private var isAdmin: Boolean = false
    private var userId = ""
    private var mSettingList: ListView? = null
    private var CancelPopUp: PopupDialog? = null
    private lateinit var adLoader: AdLoader
    private var extras: Bundle? = null
    private lateinit var progressBar: ProgressBar


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        progressBar = ProgressBar(this)

        webView = binding.webView
        webViewBtn = binding.webViewBtn
        settingBtn = binding.settingBtn
        mViewFlipper = binding.viewFlipper

        val settingBackBtn: ImageView
        val pushDialog = PushCheckDialog()
        var FLIPPERCOUNT = 0
        var intentLinkURL = ""
        var mSettingAdapter: SettingAdapter?
        val webviewclass = WebViewClientClass(this)
        val webviewInterface = WebviewInterface(this)

        if (intent.extras != null) {
            extras = intent.extras!!
        }

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
            // Do something
        } else {
            pushDialog.show(
                supportFragmentManager, "pushDialog"
            )
        }

        intentLinkURL = if (extras?.getString("linkURL") == null) {
            "https://dunni.co.kr/"
        } else {
            extras!!.getString("linkURL")
        }!!

        mSettingAdapter = SettingAdapter(getSettingModelList(isAdmin, "userId"))
        val mItemClickListener = OnItemClickListener { parent, view, position, l_position ->
            val tv = parent.adapter.getItemId(position).toString()
            if (tv.equals("2")) {
                val intent = Intent(applicationContext, AdminPushForFirebaseActivity::class.java)
                intent.putExtra(
                    resources.getString(R.string.webViewLinkUrl), webView!!.url.toString()
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                this.startActivity(intent)
            } else if (tv.equals("1")) {
                val intent = Intent(applicationContext, testActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                this.startActivity(intent)
            } else {
                if(userId=="로그인이 필요합니다") {
                    webView!!.loadUrl("https://m.dunni.co.kr/member/login.html")
                } else {
                    webView!!.loadUrl("https://m.dunni.co.kr/member/modify.html")
                }
                FLIPPERCOUNT -= 1
                mViewFlipper?.showPrevious()

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
        // AdLoader를 생성하여 광고를 로드하는 부분
        adLoader =
            AdLoader.Builder(this, "ca-app-pub-3940256099942544/6300978111").forNativeAd { ad ->
                    if (adLoader.isLoading) {
                        // 광고 로딩 중인 경우에 대한 동작
                        // 프로그레스 바를 추가합니다.
                        progressBar.isIndeterminate = true
                        progressBar.visibility = View.VISIBLE
                        CancelPopUp!!.binding!!.myTemplate.addView(progressBar)
                    } else {
                        if (CancelPopUp?.binding != null) {
                        // 광고 로딩이 완료되어 광고를 표시할 준비가 된 경우에 대한 동작
                            CancelPopUp!!.binding!!.myTemplate.setNativeAd(ad)
                        // 프로그레스 바를 제거합니다.
                            CancelPopUp!!.binding!!.myTemplate.removeView(progressBar)
                        } else {
                        }
                    }
                }.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        // 광고 로딩 실패에 대한 동작
                        // 실패 처리를 로깅하거나 UI를 변경하는 등의 동작을 수행할 수 있습니다.
                        if(progressBar != null) {
                            progressBar.visibility = View.GONE
                        }
                        Toast.makeText(applicationContext, "광고 로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }).withNativeAdOptions(NativeAdOptions.Builder().build()).build()



        webView!!.webViewClient = webviewclass
        val webViewScrollListener =
            object : CustomWebView.WebViewScrollListener, OnScrollChangeListener {
                var params = binding.viewFlipper.layoutParams as LinearLayout.LayoutParams
                override fun scrollDown() {
                    params.weight = 10f
                    // 하단 바가 사라지는 애니메이션 추가
                    binding.bottomBtnLayout.animate().alpha(0f).setDuration(500).start()
                    binding.bottomBtnLayout.visibility = View.GONE
                }

                override fun scrollUp() {
                    params.weight = 9.2f
                    // 하단 바가 나타나는 애니메이션 추가
                    binding.bottomBtnLayout.animate().alpha(1f).setDuration(500).start()
                    binding.bottomBtnLayout.visibility = View.VISIBLE
                }

                override fun onScrollChange(
                    view: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int
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
                    if (sharedPref!!.getPrefBool(PushConstants.PUSH_SUBSCRIBED_SYSTEM)) "동의" else "미동의"
                var marketingInfo: String =
                    if (sharedPref!!.getPrefBool(PushConstants.PUSH_SUBSCRIBED_MARKETING)) "동의" else "미동의"
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
