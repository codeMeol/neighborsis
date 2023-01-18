package com.example.neighborsis.retrofit2

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.neighborsis.retrofit2.Constants.PushConstants.Companion.BASE_URL
import com.example.neighborsis.retrofit2.RetrofitInterface.NotificationAPI
import com.example.neighborsis.util.Pkce
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy {
            retrofit.create(NotificationAPI::class.java)
        }
    }
}