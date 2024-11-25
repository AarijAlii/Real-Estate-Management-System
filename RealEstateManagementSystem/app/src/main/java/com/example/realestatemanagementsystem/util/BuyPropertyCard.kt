package com.example.realestatemanagementsystem.util


import android.app.Application
import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.realestatemanagementsystem.AppDatabase
import com.example.realestatemanagementsystem.Appointment.AppointmentViewModel
import com.example.realestatemanagementsystem.Property.Property
import com.example.realestatemanagementsystem.favorites.FavoriteViewModel
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun BuyPropertyCards(
    appointmentViewModel: AppointmentViewModel,
    modifier: Modifier = Modifier,
    email: String,
    propertyId: Int,
    property: Property,
    navHostController: NavHostController,
    onCompare: (Property?) -> Unit,
    favoriteViewModel: FavoriteViewModel,
    onclick: () -> Unit,
    userProfileViewModel : UserProfileViewModel
) {
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }
    val imageDao = AppDatabase.getDatabase(LocalContext.current).imageDao()

    val userProfile by userProfileViewModel.appointmentSellerProfile.collectAsState()
    LaunchedEffect(property.email){
        userProfileViewModel.getuserProfileForAppointment(property.email)
    }




    val coroutineScope = rememberCoroutineScope()
    val isLoading = remember { mutableStateOf(false) }
    val favoriteState = remember { mutableStateOf(false) }
    val imageUrls = remember { mutableStateOf<List<String>>(emptyList()) }
    LaunchedEffect(email, propertyId) {
        favoriteState.value = favoriteViewModel.isFavorite(email, propertyId)

    }
    LaunchedEffect(propertyId){
        imageUrls.value=imageDao.getImageUrlsForProperty(propertyId)

    }

    val context = LocalContext.current
    val application = context.applicationContext as Application

    // Fetch images for the property




    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(450.dp)
            .clickable { onclick() }
            .shadow(8.dp, RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                ,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Display Images
            if (imageUrls.value.isNotEmpty()) {
                // Use rememberAsyncImagePainter to cache the image

             DisplayImages(imageUrls.value)
            }
            else{
                Box(modifier=Modifier.height(200.dp).fillMaxWidth(), contentAlignment = Alignment.Center){
                    Text("No Image Available")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Price and Area Information
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PKR ${formatPrice(property.price)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${property.area} yd\u00B2", // Use Unicode for superscript ²
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Bedroom and Bathroom Information
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${property.bedrooms} Bedrooms",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
                Text(
                    text = "${property.rooms} Bathrooms",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // City and State
            Text(
                text = "${property.city}, ${property.state}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.weight(1f))

            // Buttons: Buy and Favorite
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick={onCompare(property)
                }, colors = ButtonColors(
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    containerColor = Color.Red,
                    disabledContentColor = Color.White,
                ),
                    modifier = Modifier.weight(1f).padding(8.dp)){
                    Text(text = "Add to Compare")
                }
                Button(
                    onClick = {
                        isDialogOpen = true

                    },
                    colors = ButtonColors(
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        containerColor = Color.Red,
                        disabledContentColor = Color.White,
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Make Appointment")
                }

                IconButton(
                    onClick = {
                        if (isLoading.value) return@IconButton
                        val previousState = favoriteState.value
                        favoriteState.value = !previousState
                        isLoading.value = true

                        coroutineScope.launch {
                            try {
                                favoriteViewModel.addOrRemoveFavorite(email, propertyId)
                            } catch (e: Exception) {
                                favoriteState.value = previousState
                                Log.e("FavoriteError", "Error toggling favorite: ${e.message}")
                            } finally {
                                isLoading.value = false
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (favoriteState.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.Red
                    )
                }
            }}
        }
    if (isDialogOpen) {
        DatePickerDialog(onDateSelected = { date->
            selectedDate = date


        }, onDismiss = { isDialogOpen = false
            val formattedDate = selectedDate?.let {
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                sdf.format(it)  // Format the selected date
            } ?: "No date selected"
             appointmentViewModel.insertAppointment(propertyId=propertyId, buyerEmail = email, ownerEmail = property.email, date = formattedDate)
        })

    }

    }



fun formatPrice(price: Double): String {
    return when {
        price >= 1_00_00_000 -> "${(price / 1_00_00_000).toInt()} Crore"
        price >= 1_00_000 -> "${(price / 1_00_000).toInt()} Lacs"
        else -> price.toInt().toString()
    }
}
@Composable
fun DatePickerDialog(
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit
) {
    val currentDate = Calendar.getInstance()
    val initialYear = currentDate.get(Calendar.YEAR)
    val initialMonth = currentDate.get(Calendar.MONTH)
    val initialDay = currentDate.get(Calendar.DAY_OF_MONTH)

    // Show date picker dialog
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            DatePicker(
                year = initialYear,
                month = initialMonth,
                dayOfMonth = initialDay,
                onDateSelected = onDateSelected,
                onDismiss = onDismiss
            )
        }
    }
}
@Composable
fun DatePicker(
    year: Int,
    month: Int,
    dayOfMonth: Int,
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, selectedYear, selectedMonth, selectedDay ->
            // Creating a Calendar instance with the selected year, month, and day
            val selectedDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)  // month is zero-indexed
                set(Calendar.DAY_OF_MONTH, selectedDay)
            }.time

            // Call the onDateSelected callback with the selected date
            onDateSelected(selectedDate)

            // Dismiss the dialog
            onDismiss()
        },
        year,
        month,
        dayOfMonth
    )

    datePickerDialog.show()
}
//@Composable
//fun MyDatePicker() {
//    var selectedDate by remember { mutableStateOf("") }
//    var isDialogOpen by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(text = "Selected Date: $selectedDate")
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Button(onClick = { isDialogOpen = true }) {
//            Text(text = "Select Date")
//        }
//
//        if (isDialogOpen) {
//            DatePickerDialog(
//                onDateSelected = { date ->
//                    selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
//                    isDialogOpen = false
//                },
//                onDismiss = { isDialogOpen = false }
//            )
//        }
//    }
//}