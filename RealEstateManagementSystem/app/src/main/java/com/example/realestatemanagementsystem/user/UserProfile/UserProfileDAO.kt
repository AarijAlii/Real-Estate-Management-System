package com.example.realestatemanagementsystem.user.UserProfile

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userProfile: UserProfile)


    @Query("SELECT * FROM user_profile WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserProfile?


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
//    @Query("SELECT * FROM user_profile ")
//    fun getUserAllProfile(): LiveData<UserProfile>
}


