package com.example.realestatemanagementsystem.Appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanagementsystem.Appoitnment.AppointmentDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppointmentViewModel(private val appointmentDao: AppointmentDao) : ViewModel() {

    private val _upComingAppointment =  MutableStateFlow<List<AppointmentDetails>>(emptyList())
    val upComingAppointment : StateFlow<List<AppointmentDetails>> get()=_upComingAppointment

    private val _upComingAppointmentSeller =  MutableStateFlow<List<AppointmentDetailsSeller>>(emptyList())
    val upComingAppointmentSeller : StateFlow<List<AppointmentDetailsSeller>> get()=_upComingAppointmentSeller



    fun insertAppointment(propertyId: Int, ownerEmail: String, buyerEmail: String, date: String) {
        viewModelScope.launch {
            appointmentDao.insertAppointment(propertyId, ownerEmail, buyerEmail, date)
        }
    }

    fun getAppointmentsByBuyer(buyerEmail: String) {
        viewModelScope.launch {
        _upComingAppointment.value=appointmentDao.getAppointmentsForBuyer(buyerEmail)}
    }
    fun getAppointmentsByOwner(ownerEmail: String) {
        viewModelScope.launch {
            _upComingAppointmentSeller.value=appointmentDao.getAppointmentsForOwner(ownerEmail)
        }
    }

}
