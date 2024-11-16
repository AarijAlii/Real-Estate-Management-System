package com.example.realestatemanagementsystem.user.UserProfile

import androidx.room.*
import androidx.room.Dao
import androidx.room.Query

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userProfile: UserProfile)

//    @Query("DELETE FROM user_profile WHERE id = :id")
//    suspend fun clearProfile(id: Int)

    @Query("SELECT * FROM user_profile WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserProfile?
    //:email LIMIT 1"
}