//package com.example.realestatemanagementsystem.review
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//
//class ReviewViewModelFactory(private val reviewDao: ReviewDao) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return ReviewViewModel(reviewDao) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
