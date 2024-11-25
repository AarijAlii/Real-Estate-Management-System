package com.example.realestatemanagementsystem.Appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AppointmentViewModelFactory(private val appointmentDao: AppointmentDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppointmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppointmentViewModel(appointmentDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
