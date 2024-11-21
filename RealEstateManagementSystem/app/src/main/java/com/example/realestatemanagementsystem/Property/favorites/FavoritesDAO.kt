package com.example.realestatemanagementsystem.property.favorites

import androidx.room.Dao
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Query(
        """
        INSERT INTO favorites (email, propertyId) 
        VALUES (:email, :propertyId)
        """
    )
    suspend fun addFavorite(email: String, propertyId: Int)

    @Query("DELETE FROM favorites WHERE email = :email AND propertyId = :propertyId")
    suspend fun removeFavorite(email: String, propertyId: Int)

    @Query("SELECT * FROM favorites WHERE email = :email")
    suspend fun getFavoritesByEmail(email: String): List<Favorite>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE email = :email AND propertyId = :propertyId)")
    suspend fun isFavorite(email: String, propertyId: Int): Boolean
}
