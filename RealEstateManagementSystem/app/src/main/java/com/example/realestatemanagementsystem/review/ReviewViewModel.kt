package com.example.realestatemanagementsystem.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewViewModel(private val reviewDao: ReviewDao) : ViewModel() {

    // Insert Review
    fun insertReview(
        contractorId: Int,
        propertyId: Int,
        email: String,
        rating: Int,
        comment: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            reviewDao.insertReview(contractorId, propertyId, email, rating, comment)
        }
    }

    // Fetch Reviews for Contractor
    suspend fun getReviewsForContractor(contractorId: Int) = reviewDao.getReviewsForContractor(contractorId)

    // Delete Review
//    fun deleteReview(reviewId: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            reviewDao.deleteReview(reviewId)
//        }
//    }
}
