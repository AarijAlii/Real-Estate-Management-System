package com.example.realestatemanagementsystem.image

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.imgur.com/3/image"  // Imgur API base URL

    // Create Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  // Add Gson Converter
            .build()
    }

    // Create Imgur API service instance
    val imgurApiService: ImgurApiService by lazy {
        retrofit.create(ImgurApiService::class.java)
    }
}
