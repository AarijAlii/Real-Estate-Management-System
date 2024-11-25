package com.example.realestatemanagementsystem.Home.Screens

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.realestatemanagementsystem.AppDatabase
import com.example.realestatemanagementsystem.Appointment.AppointmentDetails
import com.example.realestatemanagementsystem.Appointment.AppointmentDetailsSeller
import com.example.realestatemanagementsystem.Appointment.AppointmentViewModel
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import com.example.realestatemanagementsystem.util.DisplayImages
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun AppointmentScreen(modifier: Modifier = Modifier,
                      appointmentViewModel: AppointmentViewModel,
                      email:String,
                      userProfileViewModel: UserProfileViewModel,
                      innerPadding: PaddingValues
) {
    val tabs = listOf("Buy Appointments", "Sell Appointments")
    val pagerState = rememberPagerState(initialPage = 0){tabs.size}
    val scope= rememberCoroutineScope()


    Column(modifier = modifier.padding(innerPadding)) {


        TabRow(
            selectedTabIndex = pagerState.currentPage,

            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = Color.Red
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title, color = Color.Black) },
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                )
            }
        }

        // HorizontalPager for swiping between tabs
        HorizontalPager(
            state=pagerState,
        ){page ->
            when (page) {
                0 ->    BuyingAppointment(appointmentViewModel,email,userProfileViewModel)
                1-> SellingAppointment(appointmentViewModel,email,userProfileViewModel)
                }}




    }
}
@Composable
fun BuyingAppointment(appointmentViewModel: AppointmentViewModel,email:String,userProfileViewModel: UserProfileViewModel,){
    LaunchedEffect(Unit) {
        appointmentViewModel.getAppointmentsByBuyer(email)
    }
    val upcomingAppointments by appointmentViewModel.upComingAppointment.collectAsState()

    Spacer(modifier = Modifier.padding(4.dp))

    if(!upcomingAppointments.isEmpty()){
        LazyColumn {items(upcomingAppointments){upcomingAppointment->

            AppointmentCard(modifier = Modifier, appointment = upcomingAppointment, userProfileViewModel = userProfileViewModel)

        }

        }}
}
@Composable
fun AppointmentCard(modifier: Modifier = Modifier, appointment: AppointmentDetails, userProfileViewModel: UserProfileViewModel) {
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
@Composable
fun SellingAppointment(appointmentViewModel: AppointmentViewModel,email:String,userProfileViewModel: UserProfileViewModel,){
    LaunchedEffect(Unit) {
        appointmentViewModel.getAppointmentsByOwner(email)
    }
    val upcomingAppointments by appointmentViewModel.upComingAppointmentSeller.collectAsState()

    Spacer(modifier = Modifier.padding(4.dp))

    if(!upcomingAppointments.isEmpty()){
        LazyColumn {items(upcomingAppointments){upcomingAppointment->

            AppointmentCardSeller(modifier = Modifier, appointment = upcomingAppointment, userProfileViewModel = userProfileViewModel)

        }

        }}
}
@Composable
fun AppointmentCardSeller(modifier: Modifier = Modifier, appointment: AppointmentDetailsSeller, userProfileViewModel: UserProfileViewModel) {
    val appointmentSellerProfile by userProfileViewModel.appointmentSellerProfile.collectAsState()
    userProfileViewModel.getuserProfileForAppointment(appointment.buyerEmail)
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
                Text(text = appointment.buyerEmail,style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = "${appointmentSellerProfile?.contact}",style = MaterialTheme.typography.bodyMedium)

        }}
}