package com.example.realestatemanagementsystem.user.UserProfile

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile",
    indices = [Index(value = ["contact"], unique = true)])
data class UserProfile(
    @PrimaryKey val email: String,
    val firstName: String = "",
    val lastName: String= "",
    val contact: String = "",
    val city: String = "",
    val region: String = "",
    val postalCode: String = "",
    val rating: Int = 0,
    val imageUrl: String = ""
)
