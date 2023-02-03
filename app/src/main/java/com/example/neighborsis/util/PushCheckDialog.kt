package com.example.neighborsis.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.neighborsis.R
import com.example.neighborsis.retrofit2.Constants.PushConstants

class PushCheckDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("이벤트 알림 및 시스템 알림 푸시에 동의 하시겠습니까?")

                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@OnClickListener
                        with (sharedPref.edit()) {
                            putBoolean(PushConstants.PUSH_SUBSCRIBED_NONE,true)
                            putBoolean(PushConstants.PUSH_SUBSCRIBED_MARKETING,false)
                            putBoolean(PushConstants.PUSH_SUBSCRIBED_SYSTEM,false)
                            apply()
                        }
                    })
                .setPositiveButton("동의",
                    DialogInterface.OnClickListener { dialog, id ->
                        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@OnClickListener
                        with (sharedPref.edit()) {
                            putBoolean(PushConstants.PUSH_SUBSCRIBED_MARKETING,true)
                            putBoolean(PushConstants.PUSH_SUBSCRIBED_SYSTEM,true)
                            apply()
                        }
                        Log.d("준영테스트","Marketing = ${sharedPref.getBoolean(PushConstants.PUSH_SUBSCRIBED_MARKETING, false)}" +
                                "System = ${sharedPref.getBoolean(PushConstants.PUSH_SUBSCRIBED_SYSTEM, false)} ")
                    })
                .setIcon(R.drawable.dunny_icon)
                .setTitle("푸시알림 동의 받기")
            // Create the AlertDialog object and return it

            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}