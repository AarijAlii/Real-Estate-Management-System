package com.example.realestatemanagementsystem.review

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review WHERE contractorId = :contractorId")
    suspend fun getReviewsForContractor(contractorId: Int): List<Review>

    @Query("""
        INSERT INTO review (contractorId, email, rating, comment) 
        VALUES (:contractorId, :userEmail, :rating, :comment)
    """)
    suspend fun insertReview(contractorId: Int, userEmail: String, rating:Int, comment: String)

    @Query("SELECT AVG(rating) FROM review WHERE contractorId = :contractorId")
    suspend fun calculateAverageRating(contractorId: Int): Float
}