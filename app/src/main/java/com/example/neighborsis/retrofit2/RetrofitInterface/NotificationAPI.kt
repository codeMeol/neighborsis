package com.example.neighborsis.retrofit2.RetrofitInterface


import com.example.neighborsis.dataclass.PushNotification

import com.example.neighborsis.retrofit2.Constants.PushConstants.Companion.CONTENT_TYPE
import com.example.neighborsis.retrofit2.Constants.PushConstants.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>

    //https://{mall_id}.cafe24api.com/api/v2/oauth/authorize?response_type=code&client_id={client_id}&state={encode_csrf_token}
    //&redirect_uri={encode_redirect_uri}&scope={scope}


}