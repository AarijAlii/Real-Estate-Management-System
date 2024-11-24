package com.example.realestatemanagementsystem.image

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ImageDao {

    @Query("""
        INSERT OR REPLACE INTO property_images (propertyId, imageUrl)
        VALUES (:propertyId, :imageUrl)
    """)
    suspend fun insertImage(propertyId: Long, imageUrl: String)

    @Transaction
    suspend fun insertImages(propertyId:Long,images: List<ImageEntity>) {
        images.forEach { image ->
            insertImage(image.propertyId, image.imageUrl)
        }
    }
}