package com.example.realestatemanagementsystem.Property

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile

@Immutable
@Entity(
    tableName = "property",
    foreignKeys = [
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["email"],
            childColumns = ["email"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Property(
    @PrimaryKey(autoGenerate = true) val propertyId: Int = 0,
    val city: String,
    val state: String,
    val propertyNumber: String,
    val rooms: Int,
    val bedrooms: Int,
    val garage: Int,
    val area: Double,
    val type: String,
    val price: Double,
    val zipCode: String,
    val email: String, // FK to identify the user
    val isSold: Boolean = false // To track sold status
)
