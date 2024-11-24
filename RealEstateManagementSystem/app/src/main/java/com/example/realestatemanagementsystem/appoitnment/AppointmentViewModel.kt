package com.example.realestatemanagementsystem.appoitnment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AppointmentViewModel(private val appointmentDao: AppointmentDao) : ViewModel() {

    fun insertAppointment(propertyId: Int, ownerEmail: String, buyerEmail: String, date: String) {
        viewModelScope.launch {
            appointmentDao.insertAppointment(propertyId, ownerEmail, buyerEmail, date)
        }
    }

    fun getAppointmentsByBuyer(buyerEmail: String): Flow<List<Appointment>> {
        return appointmentDao.getAppointmentsByBuyer(buyerEmail)
    }

    fun getAppointmentsByOwner(ownerEmail: String): Flow<List<Appointment>> {
        return appointmentDao.getAppointmentsByOwner(ownerEmail)
    }
}
