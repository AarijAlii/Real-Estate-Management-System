package com.example.realestatemanagementsystem.contractor

data class ContractorWithUserProfile(
    val contractorId: Int,
    val email: String,
    val experience: String,
    val rate: String,
    val speciality: String,
    val overallRating: Float,
    val firstName: String,
    val lastName: String,
    val imageUrl: String
)
