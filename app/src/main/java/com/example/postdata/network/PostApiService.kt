package com.example.postdata.network

import com.example.postdata.model.Users
import okhttp3.Call
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

private const val BASE_URL = "https://reqres.in/api/"

//creating a retrofit builder
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface PostApiService{

    @POST("users")
    suspend fun postData(
        @Body userData: Users?

    ): Response<Users>
}
object PostApi{
    val retrofitService: PostApiService by lazy {
        retrofit.create(PostApiService::class.java)
    }
}