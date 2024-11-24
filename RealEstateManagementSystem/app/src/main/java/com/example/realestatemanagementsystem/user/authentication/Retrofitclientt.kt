package com.example.realestatemanagementsystem.user.authentication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//http://your-ngrok-url-or-localhost:3000/
object RetrofitClientt {
    private const val BASE_URL = "https://29c0-2400-adc1-164-5700-396a-8283-9815-5871.ngrok-free.app"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api:  AuthApiService = retrofit.create( AuthApiService::class.java)
}
