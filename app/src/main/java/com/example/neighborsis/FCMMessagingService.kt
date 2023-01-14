package com.example.neighborsis

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.neighborsis.activity.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FCMMessagingService() : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task
            // Log and toast
            val msg = getString(R.string.msg_token_fmt2, token)
            Log.d(ContentValues.TAG, msg)



        })

    }

    override fun onNewToken(token: String) {
        Log.d("onNewToken", "${token}")
        super.onNewToken(token)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("준영테스트", "onMessageReceived: ${remoteMessage}")
        super.onMessageReceived(remoteMessage)

        if(remoteMessage.notification!=null){
            Log.d("준영테스트","알림 메시지, ${remoteMessage.notification!!.body}")
            val messagebody = remoteMessage.notification!!.body
            val messagetitle = remoteMessage.notification!!.title
            val intent=Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messagetitle)
                .setContentText(messagebody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
            var notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            Log.d("준영테스트", "Create channel to show notifications.  ${remoteMessage}")


            notificationManager!!.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }
            notificationManager.notify(channelId.toInt(),notificationBuilder.build())
        }

        if (remoteMessage.data.isNotEmpty()) {
            Log.d("준영테스트", "Message data payload: ${remoteMessage.data}")
//            val intent = Intent(baseContext, FCMMessagingService::class.java)
//            intent.putExtra("EXTRA_SESSION_ID", sessionId)
//            startActivity(intent)
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                Log.d("준영테스트","방갑습니다.")
            } else {
                // Handle message within 10 seconds
            }
        }

        // Push Data 로 넘어 왔을 시 처
//        if (intent?.extras != null) {
//            for (key: String in intent!!.extras!!.keySet()) {
//                val value = intent!!.extras!!.get(key)
//                Log.d(Constants.LOG_TAG, "Key: ," + key + " Value: " + value);
//            }
//        }
    }
}