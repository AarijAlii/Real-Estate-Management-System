package com.example.realestatemanagementsystem.user.UserProfile

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import androidx.room.Query

@Dao
interface UserProfileDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertOrUpdate(userProfile: UserProfile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userProfile: UserProfile)

//    @Query("DELETE FROM user_profile WHERE id = :id")
//    suspend fun clearProfile(id: Int)


//    @Query("SELECT * FROM user_profile WHERE id = :id LIMIT 1")
//    fun getUserProfile(id: Int): LiveData<UserProfile>


//    @Query("SELECT * FROM user_profile WHERE email = :email LIMIT 1")
//    suspend fun getUserProfileByEmail(email: String): UserProfile?


    @Query("SELECT * FROM user_profile WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserProfile?


//    @Query("SELECT * FROM user_profile ")
//    fun getUserAllProfile(): LiveData<UserProfile>



}