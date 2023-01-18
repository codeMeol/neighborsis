package com.example.neighborsis.retrofit2.RetrofitInterface

import com.example.neighborsis.DTO.PostResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface CafeAutorizationAPI {
//
//
@GET("{post}")
open fun getPosts(@Path("post") post: String)
}