package com.example.realestatemanagementsystem.review

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanagementsystem.contractor.ContractorDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val reviewDao: ReviewDao,
    private val contractorDao: ContractorDao
) : ViewModel() {

    fun submitReview(contractorId: Int, userEmail: String, rating: Float, comment: String) {
        viewModelScope.launch {
            try {
                // Insert the review
                reviewDao.insertReview(
                    contractorId = contractorId,
                    userEmail = userEmail,
                    rating = rating,
                    comment = comment
                )

                // Recalculate the average rating
                val averageRating = reviewDao.calculateAverageRating(contractorId)

                // Update the contractor's overall rating
                contractorDao.updateOverallRating(contractorId, averageRating)
            } catch (e: Exception) {
                Log.e("ReviewViewModel", "Error submitting review", e)
            }
        }
    }
}
