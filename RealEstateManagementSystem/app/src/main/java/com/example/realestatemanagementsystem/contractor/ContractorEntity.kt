package com.example.realestatemanagementsystem.contractor

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile


@Entity(tableName = "contractor",
    foreignKeys = [
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["email"],
            childColumns = ["email"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Contractor(
    @PrimaryKey(autoGenerate = true) val contractorId: Int = 0,
    val email: String,
    val experience: String,
    val rate: String,
    val speciality: String,
    val overallRating: Float = 0.0f,
    val imageUrl: String? = null
)
