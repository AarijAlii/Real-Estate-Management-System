package com.example.realestatemanagementsystem.user.UserProfile

import retrofit2.http.*
import retrofit2.Call

interface UserProfileApi {
    // POST request to create a user profile
    @POST("userprofile")
    fun createUserProfile(@Body userProfile: UserProfile): Call<UserProfile>

    // GET request to fetch a user profile by email
    @GET("userprofile/{email}")
    fun getUserProfileByEmail(@Path("email") email: String): Call<UserProfile>

    @PUT("user_profile/{email}")
    fun updateUserProfile(
        @Path("email") email: String,
        @Body userProfile: UserProfile
    ): Call<UserProfile>

}
