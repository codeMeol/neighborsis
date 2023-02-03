package com.example.neighborsis.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import com.example.neighborsis.R
import com.example.neighborsis.SharedPreferenceClass.Sharedpref
import com.example.neighborsis.databinding.ActivityTestBinding
import com.example.neighborsis.retrofit2.Constants.PushConstants

class testActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var mSharedPref: Sharedpref? = Sharedpref("준영테스트")


        binding.MarketingSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                mSharedPref!!.save(PushConstants.PUSH_SUBSCRIBED_MARKETING, true)
            } else {
                mSharedPref!!.save(PushConstants.PUSH_SUBSCRIBED_MARKETING, false)
            }
        })
        binding.SystemSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                mSharedPref!!.save(PushConstants.PUSH_SUBSCRIBED_SYSTEM, true)
            } else {
                mSharedPref!!.save(PushConstants.PUSH_SUBSCRIBED_SYSTEM, false)
            }
        })

    }
}
