package com.example.realestatemanagementsystem.Appointment

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.realestatemanagementsystem.Property.Property
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile

@Entity(
    tableName = "appointments",
    foreignKeys = [
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["email"],
            childColumns = ["ownerEmail"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["email"],
            childColumns = ["buyerEmail"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Property::class,
            parentColumns = ["propertyId"],
            childColumns = ["propertyId"],
            onDelete = ForeignKey.CASCADE
        ),

    ]
)
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    val appointmentId: Int = 0,
    val propertyId: Int,
    val ownerEmail: String,
    val buyerEmail: String,

    val date: String
)
