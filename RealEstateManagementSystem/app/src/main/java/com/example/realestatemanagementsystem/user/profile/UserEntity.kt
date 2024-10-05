package com.example.realestatemanagementsystem.user.profile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firstName: String = "",
    val lastName: String= "",
    val contact: String = "",
    val city: String = "",
    val region: String = "",
    val postalCode: String = "",
    val rating: Int = 0
)
