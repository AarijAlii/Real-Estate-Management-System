package com.example.realestatemanagementsystem.Appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppointmentViewModel(private val appointmentDao: AppointmentDao) : ViewModel() {

    private val _upComingAppointment =  MutableStateFlow<List<Appointment>>(emptyList())
    val upComingAppointment : StateFlow<List<Appointment>> get()=_upComingAppointment

    fun insertAppointment(propertyId: Int, ownerEmail: String, buyerEmail: String, date: String) {
        viewModelScope.launch {
            appointmentDao.insertAppointment(propertyId, ownerEmail, buyerEmail, date)
        }
    }

    fun getAppointmentsByBuyer(buyerEmail: String) {
        viewModelScope.launch {
        _upComingAppointment.value=appointmentDao.getAppointmentsByBuyer(buyerEmail)}
    }

    fun getAppointmentsByOwner(ownerEmail: String): Flow<List<Appointment>> {
        return appointmentDao.getAppointmentsByOwner(ownerEmail)
    }
}
