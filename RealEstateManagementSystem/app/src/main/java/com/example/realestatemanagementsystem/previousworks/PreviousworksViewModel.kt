package com.example.realestatemanagementsystem.previousworks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PreviousWorksViewModel(private val previousWorksDao: PreviousWorksDao) : ViewModel() {

    // Insert Previous Work
    fun insertPreviousWork(contractorId: Int, propertyId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            previousWorksDao.insertPreviousWork(contractorId, propertyId)
        }
    }

    // Fetch Previous Works for Contractor
    suspend fun getPreviousWorksForContractor(contractorId: Int) = previousWorksDao.getPreviousWorksByContractor(contractorId)

    // Delete Previous Work

}
