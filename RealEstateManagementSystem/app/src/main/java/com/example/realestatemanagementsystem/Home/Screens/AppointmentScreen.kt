package com.example.realestatemanagementsystem.Home.Screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.realestatemanagementsystem.AppDatabase
import com.example.realestatemanagementsystem.Appointment.AppointmentViewModel
import com.example.realestatemanagementsystem.Appoitnment.Appointment
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import com.example.realestatemanagementsystem.util.DisplayImages
import java.util.Date

@Composable
fun AppointmentScreen(modifier: Modifier = Modifier,
                      appointmentViewModel: AppointmentViewModel,
                      email:String,
                      userProfileViewModel: UserProfileViewModel,
                      innerPadding: PaddingValues
) {
    val upcomingAppointments by appointmentViewModel.upComingAppointment.collectAsState()
    LaunchedEffect(Unit) {
        appointmentViewModel.getAppointmentsByBuyer(email)
    }
    Log.d("apps",upcomingAppointments.toString())
    Column(modifier = modifier.padding(innerPadding)) {
        Text(text = "Upcoming Appointments", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(8.dp))
        Spacer(modifier = Modifier.padding(4.dp))
        if(!upcomingAppointments.isEmpty()){
        LazyColumn {items(upcomingAppointments){upcomingAppointment->

            AppointmentCard(modifier = modifier, appointment = upcomingAppointment, userProfileViewModel = userProfileViewModel)

        }

        }}


    }
}

@Composable
fun AppointmentCard(modifier: Modifier = Modifier, appointment: Appointment, userProfileViewModel: UserProfileViewModel) {
    val appointmentSellerProfile by userProfileViewModel.appointmentSellerProfile.collectAsState()
    userProfileViewModel.getuserProfileForAppointment(appointment.ownerEmail)
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }
    val imageDao = AppDatabase.getDatabase(LocalContext.current).imageDao()
    val imageUrls = remember { mutableStateOf<List<String>>(emptyList()) }
    LaunchedEffect(appointment.propertyId){
        imageUrls.value=imageDao.getImageUrlsForProperty(appointment.propertyId)

    }

    Card   (modifier = modifier.padding(8.dp)){
        Column(modifier = modifier.padding(8.dp)) {
            if (imageUrls.value.isNotEmpty()) {
                // Use rememberAsyncImagePainter to cache the image

                DisplayImages(imageUrls.value)
            }
            else{
                Box(modifier=Modifier.height(200.dp).fillMaxWidth(), contentAlignment = Alignment.Center){
                    Text("No Image Available")
                }
            }
            Spacer(modifier=Modifier.padding(4.dp))
            Text(text = appointment.date, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.padding(4.dp))
            Row {

                Text(text = "${appointmentSellerProfile?.firstName} ${appointmentSellerProfile?.lastName}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = appointment.ownerEmail,style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = "${appointmentSellerProfile?.contact}",style = MaterialTheme.typography.bodyMedium)

    }}
}

