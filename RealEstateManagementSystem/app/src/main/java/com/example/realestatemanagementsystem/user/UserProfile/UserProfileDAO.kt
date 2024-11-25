package com.example.realestatemanagementsystem.user.UserProfile

import android.util.Log
import androidx.room.*
import androidx.room.Dao
import androidx.room.Query

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userProfile: UserProfile)

//    @Query("DELETE FROM user_profile WHERE id = :id")
//    suspend fun clearProfile(id: Int)

    @Query("SELECT * FROM user_profile WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserProfile
    //:email LIMIT 1"

    @Update
    suspend fun updateUser(userProfile: UserProfile)


    @Query("""
        UPDATE user_profile 
        SET firstName = :firstName,
            lastName = :lastName,
            contact = :contact,
            city = :city,
            region = :region,
            postalCode = :postalCode
        WHERE email = :email
    """)
    suspend fun updateUserr(
        email: String,
        firstName: String,
        lastName: String,
        contact: String,
        city: String,
        region: String,
        postalCode: String
    )


        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertOrUpdateUserProfile(userProfile: UserProfile)




}