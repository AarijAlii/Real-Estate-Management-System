package com.example.realestatemanagementsystem.user.UserProfile

data class UuserProfile(
    val email: String,
    val password: String,
    val firstName: String = "",
    val lastName: String = "",
    val contact: String = "",
    val city: String = "",
    val region: String = "",
    val postalCode: String = "",
    val rating: Int = 0
)
