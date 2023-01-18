package com.example.neighborsis.retrofit2.RetrofitInterface

import com.example.neighborsis.retrofit2.Constants.CafeConstants
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface CafeAutorizationAPI {

    @GET("${CafeConstants.BASE_URL}.cafe24api.com/api/v2/oauth/authorize?response_type=code&client_id=${CafeConstants.CLIENT_ID}&state=3000&&redirect_url=${CafeConstants.REDIRECT_URL}&scope=mall.read_customer,mall.write_customer&code_challenge=${CODE_CHALLENGE}&code_challenge_method=S256")
    suspend fun postCafeAuthorization(

    ): Response<ResponseBody>
}