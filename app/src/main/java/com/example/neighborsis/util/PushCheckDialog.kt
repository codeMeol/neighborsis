package com.example.neighborsis.util

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.neighborsis.FCMMessagingService
import com.example.neighborsis.R
import com.example.neighborsis.SharedPreferenceClass.Sharedpref
import com.example.neighborsis.retrofit2.Constants.PushConstants

class PushCheckDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            var sharedpref =
                this.activity?.let { it1 -> Sharedpref(this.getString(R.string.pushOkLevel), it1) }
            val fcm = FCMMessagingService()
            builder.setMessage("이벤트 알림 및 시스템 알림 푸시에 동의 하시겠습니까?")

                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                        sharedpref!!.save(PushConstants.PUSH_SUBSCRIBED_INITIALIZING,true)
                        sharedpref.save(PushConstants.PUSH_SUBSCRIBED_MARKETING,false)
                        sharedpref.save(PushConstants.PUSH_SUBSCRIBED_SYSTEM,false)
                        fcm.deleteTopic(PushConstants.PUSH_SUBSCRIBED_MARKETING)
                        fcm.deleteTopic(PushConstants.PUSH_SUBSCRIBED_SYSTEM)
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("푸시 메시지 동의 여부 알림")
                        builder.setMessage(resources.getString(R.string.pushDisAgreeAlertMessage))
                        builder.setPositiveButton("OK") { dialog, which ->
                            // do something when the "OK" button is clicked
                        }
                        val dialog = builder.create()
                        dialog.show()
                    })
                .setPositiveButton("동의",
                    DialogInterface.OnClickListener { dialog, id ->
                        sharedpref!!.save(PushConstants.PUSH_SUBSCRIBED_INITIALIZING,true)
                        sharedpref!!.save(PushConstants.PUSH_SUBSCRIBED_MARKETING,true)
                        sharedpref!!.save(PushConstants.PUSH_SUBSCRIBED_SYSTEM,true)
                        fcm.addTopic(PushConstants.PUSH_SUBSCRIBED_MARKETING)
                        fcm.addTopic((PushConstants.PUSH_SUBSCRIBED_SYSTEM))
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("푸시 메시지 동의 여부 알림")
                        builder.setMessage(resources.getString(R.string.pushAgreeAlertMessage))
                        builder.setPositiveButton("OK") { dialog, which ->
                            // do something when the "OK" button is clicked
                        }
                        val dialog = builder.create()
                        dialog.show()
                    })
                .setIcon(R.drawable.dunny_icon)
                .setTitle("푸시알림 동의 받기")
            // Create the AlertDialog object and return it

            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}