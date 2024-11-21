package com.example.realestatemanagementsystem.favorites

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {

    // Add a property to the favorites list
    @Insert
    suspend fun addToFavorites(favorite: Favorite)

    // Remove a property from the favorites list
    @Query("DELETE FROM favorites WHERE email = :email AND propertyId = :propertyId")
    suspend fun removeFromFavorites(email: String,propertyId: Int)

    // Get all favorite properties by email
    @Query("SELECT * FROM favorites WHERE email = :email")
    suspend fun getFavoritesByEmail(email: String): List<Favorite>

    // Check if a property is in the favorites list for a given email
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE email = :email AND propertyId = :propertyId)")
    suspend fun isFavorite(email: String, propertyId: Int): Boolean
}