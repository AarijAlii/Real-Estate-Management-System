package com.example.realestatemanagementsystem.user.authentication

import com.example.realestatemanagementsystem.user.UserProfile.UserProfile

data class User(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val userProfile: UserProfile // Include any additional user data if needed
)
