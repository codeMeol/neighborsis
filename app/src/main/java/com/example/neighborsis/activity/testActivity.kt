package com.example.neighborsis.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import com.example.neighborsis.R
import com.example.neighborsis.databinding.ActivityTestBinding
import com.example.neighborsis.retrofit2.Constants.PushConstants

class testActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var pushOkLevel = getPreferences(MODE_PRIVATE).getString(this.getString(R.string.pushOkLevel),PushConstants.PUSH_SUBSCRIBED_NONE)

        binding.MarketingSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->

            if (b) {

                val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return@OnCheckedChangeListener
               sharedPref.edit().putString(this.getString(R.string.pushOkLevel,),"")


                }

             else {
            }
        })

    }
    }
