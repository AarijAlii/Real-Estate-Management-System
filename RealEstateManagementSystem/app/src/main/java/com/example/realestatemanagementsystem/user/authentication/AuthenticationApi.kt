package com.example.realestatemanagementsystem.user.authentication


import com.example.realestatemanagementsystem.user.UserProfile.UuserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/signup")
    suspend fun signUp(@Body user: UuserProfile): Response<SignUpResponse>

    @POST("auth/signin")
    suspend fun signIn(@Body user: SignInRequest): Response<SignInResponse>
    // ApiClient.kt

        @POST("userprofile/create")
        suspend fun createUserProfile(@Body userProfile: UuserProfile): Response<String>


}


data class SignInRequest(
    val email: String,
    val password: String
)

data class SignUpResponse(
    val message: String
)

data class SignInResponse(
    val message: String
)
