package com.example.neighborsis.retrofit2


import com.example.neighborsis.retrofit2.Constants.CafeConstants.Companion.BASE_URL
import com.example.neighborsis.retrofit2.RetrofitInterface.CafeAutorizationAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitCafeAuthorization {

    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy {
            retrofit.create(CafeAutorizationAPI::class.java)
        }
    }
}