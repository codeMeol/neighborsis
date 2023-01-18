package com.example.neighborsis.retrofit2

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.neighborsis.retrofit2.Constants.CafeConstants.Companion.BASE_URL
import com.example.neighborsis.retrofit2.RetrofitInterface.NotificationAPI
import com.example.neighborsis.util.Pkce
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    @RequiresApi(Build.VERSION_CODES.O)
    val CODE_VERIFIER = Pkce().generateCodeVerifier()
    @RequiresApi(Build.VERSION_CODES.O)
    val CODE_CHALLENGE = Pkce().generateCodeChallange(CODE_VERIFIER!!)

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