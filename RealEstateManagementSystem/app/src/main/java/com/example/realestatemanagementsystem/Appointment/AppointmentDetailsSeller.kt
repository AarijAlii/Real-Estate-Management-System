package com.example.realestatemanagementsystem.Appointment

data class AppointmentDetailsSeller(
    val appointmentId: Int,
    val date: String,
    val propertyId: Int,
    val propertyNumber: String,
    val ownerName: String,
    val ownerContact: String,
    val buyerName: String,
    val buyerContact: String,
    val buyerEmail: String


)