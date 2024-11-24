package com.example.realestatemanagementsystem.user.UserProfile


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL =  "https://57d2-2400-adc1-164-5700-396a-8283-9815-5871.ngrok-free.app"  // Replace with your hosted API URL

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
