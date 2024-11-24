package com.example.realestatemanagementsystem.user.UserProfile

data class FirestoreUserProfile(
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val contact: String = "",
    val city: String = "",
    val region: String = "",
    val postalCode: String = "",
    val rating: Int = 0
)
