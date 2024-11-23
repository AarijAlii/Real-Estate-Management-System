package com.example.realestatemanagementsystem.previousworks

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.realestatemanagementsystem.contractor.Contractor
import com.example.realestatemanagementsystem.Property.Property

@Entity(tableName = "previous_works",
    foreignKeys = [
        ForeignKey(
            entity = Property::class,
            parentColumns = ["propertyId"],
            childColumns = ["propertyId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Contractor::class,
            parentColumns = ["contractorId"],
            childColumns = ["contractorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PreviousWorks(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val contractorId: Int, // FK referencing Contractor
    val propertyId: Int // FK referencing Property
)
