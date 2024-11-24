package com.example.realestatemanagementsystem.appoitnment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyAppointmentsScreen(
    userEmail: String,
    appointmentViewModel: AppointmentViewModel
) {
    val appointments by appointmentViewModel.getAppointmentsByBuyer(userEmail)
        .collectAsState(initial = emptyList())

    LazyColumn {
        items(appointments) { appointment ->
            AppointmentItem(appointment = appointment, currentUserEmail = userEmail)
        }
    }
}

@Composable
fun AppointmentItem(appointment: Appointment, currentUserEmail: String) {
    val otherParty = if (appointment.ownerEmail == currentUserEmail) appointment.buyerEmail else appointment.ownerEmail
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Appointment with: $otherParty")
        Text(text = "Date: ${appointment.date}")
    }
}
