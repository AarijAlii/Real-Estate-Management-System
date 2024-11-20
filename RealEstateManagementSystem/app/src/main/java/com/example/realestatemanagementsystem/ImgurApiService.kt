package com.example.realestatemanagementsystem

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.*

//interface ImgurApiService {
//    @Multipart
//    @POST("image")
//    fun uploadImage(
//        @Part image: MultipartBody.Part
//    ): Call<ImgurResponse>
//}

//interface ImgurService {
//    @POST("image")
//    suspend fun uploadImage(
//        @Header("Authorization") clientId: String,
//        @Body requestBody: RequestBody
//    ): Response<ImgurUploadResponse>
//}



import retrofit2.http.Header

interface ImgurApiService {
    @Multipart
    @POST("3/image")
    suspend fun uploadImage(
        @Header("Authorization") clientId: String,
        @Part image: MultipartBody.Part
    ): Response<ImgurResponse>
}
