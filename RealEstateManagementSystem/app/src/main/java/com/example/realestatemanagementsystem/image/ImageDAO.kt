package com.example.realestatemanagementsystem.image

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {
    @Insert
    suspend fun insertImages(images: List<ImageEntity>)

    @Query("SELECT * FROM property_images WHERE propertyId = :propertyId")
    suspend fun getImagesForProperty(propertyId: Int): List<ImageEntity>
}
