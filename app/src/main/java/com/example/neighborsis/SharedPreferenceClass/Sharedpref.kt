package com.example.neighborsis.SharedPreferenceClass

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.example.neighborsis.retrofit2.Constants.PushConstants

class Sharedpref  {
    var sharedPref: SharedPreferences? = null
    var context: Context? = null
    var string :String? =null

//    sharedPref = this.getSharedPreferences(string, Context.MODE_PRIVATE)
    constructor(string: String,context: Context) {
        this.string=string
        this.context=context
    sharedPref=context!!.getSharedPreferences(string,Context.MODE_PRIVATE)
    }


    //토스트생성
    fun makeToast(context: Context) {
        Toast.makeText(
            context.applicationContext,
            "system push =${sharedPref}",
            Toast.LENGTH_SHORT
        ).show()
    }

    //sharedPreference에 저장
    fun save(string: String, boolean: Boolean) {

       val sharedPrefEditer =sharedPref!!.edit()
        sharedPrefEditer.putBoolean(string, boolean)
        sharedPrefEditer.apply()
    }

    //저장된 값 불러오기
    fun getPrefBool(string: String): Boolean {

        return sharedPref!!.getBoolean(string, false)
    }
}