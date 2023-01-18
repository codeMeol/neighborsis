package com.example.neighborsis.DTO

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.neighborsis.retrofit2.Constants.CafeConstants
import com.example.neighborsis.util.Pkce
import com.google.gson.annotations.SerializedName

class PostResult {
    @SerializedName("userId")
    private val userId = 0

    @SerializedName("id")
    private val id = 0

    // @SerializedName으로 일치시켜 주지않을 경우엔 클래스 변수명이 일치해야함
    private val title: String? = null
    // @SerializedName()로 변수명을 입치시켜주면 클래스 변수명이 달라도 알아서 매핑시켜줌
    @SerializedName("body")
    private val bodyValue: String? = null
    @RequiresApi(Build.VERSION_CODES.O)
    private val CODE_CHALLENGE = Pkce().generateCodeVerifier()
        ?.let { Pkce().generateCodeChallange(it) }
    // toString()을 Override 해주지 않으면 객체 주소값을 출력함
    @RequiresApi(Build.VERSION_CODES.O)
    override fun toString(): String {
        return "/api/v2/oauth/authorize?response_type=code&client_id=${CafeConstants.CLIENT_ID}&state=3000&&redirect_url=${CafeConstants.REDIRECT_URL}&scope=mall.read_customer,mall.write_customer&code_challenge=${CODE_CHALLENGE}&code_challenge_method=S256"
    }
}