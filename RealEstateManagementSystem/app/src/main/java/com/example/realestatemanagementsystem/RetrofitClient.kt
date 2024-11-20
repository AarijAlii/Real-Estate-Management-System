//package com.example.realestatemanagementsystem
//
//import okhttp3.OkHttpClient
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//
//object RetrofitClient {
//    private const val BASE_URL = "https://api.imgur.com/" // Imgur API base URL
//
//    private val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(OkHttpClient.Builder().build())
//            .build()
//    }
//
//    val imgurService: ImgurApiService by lazy {
//        retrofit.create(ImgurApiService::class.java)
//    }
//}
//
