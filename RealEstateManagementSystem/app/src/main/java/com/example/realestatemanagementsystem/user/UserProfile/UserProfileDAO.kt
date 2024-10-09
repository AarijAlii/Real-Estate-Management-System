package com.example.realestatemanagementsystem.user.UserProfile

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(userProfile: UserProfile)

    @Query("DELETE FROM user_profile WHERE id = :id")
    suspend fun clearProfile(id: Int)


    @Query("SELECT * FROM user_profile WHERE id = :id LIMIT 1")
    fun getUserProfile(id: Int): LiveData<UserProfile>

//    @Query("SELECT * FROM user_profile ")
//    fun getUserAllProfile(): LiveData<UserProfile>
}


