package com.example.realestatemanagementsystem.contractor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ContractorViewModelFactory(private val contractorDao: ContractorDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContractorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContractorViewModel(contractorDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
