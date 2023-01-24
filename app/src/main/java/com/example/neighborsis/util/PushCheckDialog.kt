package com.example.neighborsis.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.neighborsis.R

class PushCheckDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("푸시알림에 동의 하시겠습니까?")
                .setPositiveButton("동의",
                    DialogInterface.OnClickListener { dialog, id ->
                        val sharedPref = activity?.getSharedPreferences(
                            "pushAgree", Context.MODE_PRIVATE)
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}