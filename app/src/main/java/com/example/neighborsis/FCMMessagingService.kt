package com.example.neighborsis

import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.neighborsis.activity.MainActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging


class FCMMessagingService() : FirebaseMessagingService() {
    var channelId: String? = ""
    var channelName: String? = ""
    var url : String? =""
    val TAG :String = this.javaClass.name
    override fun onCreate() {
        super.onCreate()

        channelId = resources.getString(R.string.default_notification_channel_id)
        channelName = resources.getString(R.string.default_notification_channel_name)


    }

    override fun onNewToken(token: String) {
        Log.d("onNewToken", "${token}")
        super.onNewToken(token)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        var remoteMessageType: Boolean = remoteMessage.notification != null
        createNotification(remoteMessageType, remoteMessage)

        if (remoteMessage.data.isNotEmpty()) {
            if (/* Check if data needs to be processed by long running job */ true) {
                createNotification(remoteMessageType, remoteMessage)
            } else {
                // Handle message within 10 seconds
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "channelName"
        val channel = NotificationChannel(channelId, channelName, IMPORTANCE_HIGH).apply {
            description = "My channel description"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotification(remoteMessageType: Boolean, remoteMessage: RemoteMessage) {


            val messagebody =
                if (remoteMessageType) remoteMessage.notification!!.body else remoteMessage.data.get(
                    "title"
                )
            val messagetitle =
                if (remoteMessageType) remoteMessage.notification!!.title else remoteMessage.data.get(
                    "message"
                )

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("linkURL",remoteMessage.data.get("link"))
            url=remoteMessage.data.get("link")
             intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT

            val pendingIntent =PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent.isImmutable()
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, channelId!!)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messagetitle)
                .setContentText(messagebody)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setFullScreenIntent(pendingIntent,true)
                .setContentIntent(pendingIntent)
            var notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create channel to show notifications.

                notificationManager!!.createNotificationChannel(
                    NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_LOW
                    )
                )
            }

            notificationManager.notify(channelId!!.toInt(), notificationBuilder.build())

    }

    fun addTopic(topic:String){

        Firebase.messaging.subscribeToTopic("$topic")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }

                Log.d(TAG,"$msg")
            }
    }

    fun deleteTopic(topic: String){
        Firebase.messaging.unsubscribeFromTopic("$topic")
            .addOnCompleteListener { task ->
                var msg = "unSubscribed"
                if(!task.isSuccessful){
                    msg = "unSubscribed is failed"
                }
                Log.d(TAG,"$msg")
            }
    }

    fun sendByTopic(){

    }
}

