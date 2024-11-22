package com.example.realestatemanagementsystem.contractor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContractorViewModel(private val contractorDao: ContractorDao) : ViewModel() {
//    private val _contractors = MutableLiveData<List<Contractor>>()
//    val contractors: LiveData<List<Contractor>> get() = _contractors

//    fun getAllContractors() {
//        viewModelScope.launch {
//            try {
//                val contractorsList = contractorDao.getAllContractors()
//                _contractors.value = contractorsList
//            } catch (e: Exception) {
//                Log.e("ContractorViewModel", "Error fetching contractors", e)
//            }
//        }
//    }


    fun insertContractor(
        email: String,
        experience: String,
        contact: String,
        speciality: String,
        overallRating: Float = 0.0f,

    ) {
        viewModelScope.launch(Dispatchers.IO) {
                contractorDao.insertContractor(email, experience, contact, speciality, overallRating)
        }
    }


    suspend fun getContractorByEmail(email: String) = contractorDao.getContractorByEmail(email)


    fun deleteContractor(contractorId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            contractorDao.deleteContractor(contractorId)
        }
    }

    fun updateContractor(id: Int, experience: String, contact: String, speciality: String) {
        viewModelScope.launch {
            try {
                contractorDao.updateContractor(id, experience, contact, speciality)
                Log.d("ContractorViewModel", "Contractor updated successfully")
            } catch (e: Exception) {
                Log.e("ContractorViewModel", "Error updating contractor", e)
            }
        }
    }

    private val _contractors = MutableLiveData<List<ContractorWithUserProfile>>()
    val contractors: LiveData<List<ContractorWithUserProfile>> get() = _contractors

    fun fetchAllContractorsWithDetails() {
        viewModelScope.launch {
            try {
                val contractorsList = contractorDao.getAllContractorDetails()
                _contractors.value = contractorsList
            } catch (e: Exception) {
                Log.e("ContractorViewModel", "Error fetching contractors with details", e)
            }
        }
    }
}