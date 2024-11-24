package com.example.realestatemanagementsystem.appoitnment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import java.time.LocalDate

@Composable
fun ScheduleAppointmentScreen(
    propertyId: Int,
    ownerEmail: String,
    buyerEmail: String,
    appointmentViewModel: AppointmentViewModel,
    navController: NavController
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val today = LocalDate.now()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Select Appointment Date")
        Spacer(modifier = Modifier.height(16.dp))

        // Calendar Picker
        DatePicker(
            selectedDate = selectedDate,
            onDateChange = { date ->
                if (date >= today) selectedDate = date
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            appointmentViewModel.insertAppointment(
                propertyId = propertyId,
                ownerEmail = ownerEmail,
                buyerEmail = buyerEmail,
                date = selectedDate.toString()
            )
            navController.popBackStack()
        }) {
            Text(text = "Confirm Appointment")
        }
    }
}

// A simple date picker (use a proper library for better UI if needed).
@Composable
fun DatePicker(selectedDate: LocalDate, onDateChange: (LocalDate) -> Unit) {
    val context = LocalContext.current
    val year = selectedDate.year
    val month = selectedDate.monthValue - 1
    val day = selectedDate.dayOfMonth
//
//    AndroidView(factory = { context ->
//        DatePickerDialog(context, { _, y, m, d ->
//            onDateChange(LocalDate.of(y, m + 1, d))
//        }, year, month, day)
//    })
}
