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
    suspend fun insertReview(contractorId: Int, userEmail: String, rating: Float, comment: String)

    @Query("""
  SELECT u.firstName || ' ' || u.lastName AS user_name, R.rating, R.comment 
    FROM review R
    JOIN user_profile U ON R.email = U.email
    WHERE R.contractorId = :contractorId
""")
    suspend fun getReviewWithUserInfo(contractorId: Int): List<ReviewWithName>


    @Query("SELECT AVG(rating) FROM review WHERE contractorId = :contractorId")
    suspend fun calculateAverageRating(contractorId: Int): Float
}

