package com.example.realestatemanagementsystem.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.realestatemanagementsystem.contractor.ContractorDao

class ReviewViewModelFactory(
    private val reviewDao: ReviewDao,
    private val contractorDao: ContractorDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            return ReviewViewModel(reviewDao, contractorDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
