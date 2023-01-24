package com.example.neighborsis.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.neighborsis.databinding.ActivityAdminPushForFirebaseBinding
import com.example.neighborsis.dataclass.NotificationData
import com.example.neighborsis.dataclass.message
import com.example.neighborsis.retrofit2.RetrofitInstance
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AdminPushForFirebaseActivity : AppCompatActivity() {
    val TAG = "AdminPushForFirebaseActivity"
    var myToken : String = ""
    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = ActivityAdminPushForFirebaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            myToken = task.result

            Log.d(TAG +"토큰:", myToken)  ///나의 토큰을 알수있는 Firebase 메서드
        })


        fun pushBtn() = with(binding){
        pushBtn.setOnClickListener {
            val pushTitle = "동네언니" //타이틀 = value
            val  pushBody= pushForFirebaseBodyEdittext.text.toString() //메시지 =타이틀
            val pushLink =pushForFirebaseLinkEdittext.text.toString() //링크 =링크
            val recipientToken = myToken   /////내 토큰
            val topic = "/topics/weather"

            if(pushTitle.isNotEmpty() && pushLink.isNotEmpty() && recipientToken.isNotEmpty()) {
                message(
                    NotificationData(pushTitle,pushBody, pushLink),
                            topic
                ).also {
                    sendNotification(it)
                }
            }

        }}
        pushBtn()
    }

    @SuppressLint("LongLogTag")
    private fun sendNotification(notification: message) = CoroutineScope(Dispatchers.IO).launch {

        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.e(TAG, response.toString())
            } else {
                Log.e(TAG, response.code().toString())
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

}