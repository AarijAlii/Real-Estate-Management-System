package com.example.realestatemanagementsystem.image

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ImageDao {

    @Query("""
        INSERT OR REPLACE INTO property_images (propertyId, imageUrl)
        VALUES (:propertyId, :imageUrl)
    """)
     fun insertImage(propertyId: Long, imageUrl: String)

    @Transaction
    suspend fun insertImages(propertyId:Long,images: List<ImageEntity>) {
        images.forEach { image ->
            insertImage(image.propertyId, image.imageUrl)
        }
    }


        @Query("SELECT imageUrl FROM property_images WHERE propertyId = :propertyId")
        suspend fun getImageUrlsForProperty(propertyId: Int): List<String>

}