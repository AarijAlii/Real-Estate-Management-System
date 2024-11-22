package com.example.realestatemanagementsystem.review

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.realestatemanagementsystem.contractor.Contractor
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile

@Entity(tableName = "review",
    foreignKeys = [
        ForeignKey(
            entity = Contractor::class,
            parentColumns = ["contractorId"],
            childColumns = ["contractorId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["email"],
            childColumns = ["email"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Review(
    @PrimaryKey(autoGenerate = true) val reviewId: Int = 0,
    val contractorId: Int, // FK referencing Contract
    val email: String, // FK referencing UserProfile (user who hired)
    val rating: Int, // Rating out of 5 stars
    val comment: String
)
