package com.example.realestatemanagementsystem.review

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ReviewDao {
    @Query("INSERT INTO review (contractorId, propertyId, email, rating, comment) VALUES (:contractorId, :propertyId, :email, :rating, :comment)")
    suspend fun insertReview(contractorId: Int, propertyId: Int, email: String, rating: Int, comment: String)

    @Query("SELECT * FROM review WHERE contractorId = :contractorId")
    suspend fun getReviewsForContractor(contractorId: Int): List<Review>

    @Query("SELECT AVG(rating) FROM review WHERE contractorId = :contractorId")
    suspend fun getAverageRating(contractorId: Int): Float
}
