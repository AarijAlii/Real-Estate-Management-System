package com.example.realestatemanagementsystem.property.favorites

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.realestatemanagementsystem.property.Property
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile

@Entity(tableName = "favorites",
    foreignKeys = [
        ForeignKey(
            entity = Property::class,
            parentColumns = ["propertyId"],
            childColumns = ["propertyId"],
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
data class Favorite(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String, // Foreign key referencing the User profile
    val propertyId: Int // Foreign key referencing the Property table
)


