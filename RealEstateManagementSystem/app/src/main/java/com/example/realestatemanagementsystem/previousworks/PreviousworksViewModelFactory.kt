package com.example.realestatemanagementsystem.previousworks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PreviousWorksViewModelFactory(private val previousWorksDao: PreviousWorksDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreviousWorksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PreviousWorksViewModel(previousWorksDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
