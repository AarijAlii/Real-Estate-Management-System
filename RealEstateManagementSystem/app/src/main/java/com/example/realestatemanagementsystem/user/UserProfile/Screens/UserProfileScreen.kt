package com.example.realestatemanagementsystem.user.UserProfile.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileDao
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel

//@Composable
//fun UserProfileScreen(
//    viewModel: UserProfileViewModel
//) {
//    // State variables for the user input
//    var firstName by remember { mutableStateOf("") }
//    var lastName by remember { mutableStateOf("") }
//    var contact by remember { mutableStateOf("") }
//    var city by remember { mutableStateOf("") }
//    var region by remember { mutableStateOf("") }
//    var postalCode by remember { mutableStateOf("") }
//    Column(verticalArrangement = Arrangement.Center,modifier = Modifier
//
//        .padding(16.dp)) {
//
//        Text("Create Profile", fontSize = 34.sp, fontWeight = FontWeight.Bold,modifier=Modifier.padding(vertical = 16.dp))
//
//        Column(
//
//            verticalArrangement = spacedBy(16.dp)
//        ) {
//            TextField(
//                value = firstName,
//                onValueChange = { firstName = it },
//                label = { Text("First Name") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            TextField(
//                value = lastName,
//                onValueChange = { lastName = it },
//                label = { Text("Last Name") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            TextField(
//                value = contact,
//                onValueChange = { contact = it },
//                label = { Text("Contact") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            TextField(
//                value = city,
//                onValueChange = { city = it },
//                label = { Text("City") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            TextField(
//                value = region,
//                onValueChange = { region = it },
//                label = { Text("Region") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            TextField(
//                value = postalCode,
//                onValueChange = { postalCode = it },
//                label = { Text("Postal Code") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                modifier = Modifier.fillMaxWidth(),
//
//
//            )
//
//            Button(
//                onClick = {
//                    // Create a UserProfile instance
//                    val userProfile = UserProfile(
//                        firstName = firstName,
//                        lastName = lastName,
//                        contact = contact,
//                        city = city,
//                        region = region,
//                        postalCode = postalCode,
//                        rating = 0 // Set a default value for rating or adjust as needed
//                    )
//
//                    // Save the user profile using the ViewModel
//                    viewModel.saveProfile(userProfile)
//                },
//                colors = ButtonColors(
//                    contentColor = Color.White,
//                    disabledContainerColor = Color.Gray,
//                    containerColor = Color.Red,
//                    disabledContentColor = Color.White,
//                ) ,
//                modifier = Modifier.fillMaxWidth()
//
//            ) {
//                Text("Save")
//            }
//        }
//    }
//}





//FIREBASE CONNECTED
//@Composable
//fun ProfileScreen(
//    email: String,
//    userProfileDao: UserProfileDao
//) {
//    // State to hold the user profile data
//    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
//    var isLoading by remember { mutableStateOf(true) }
//    var errorMessage by remember { mutableStateOf("") }
//
//    // Load the user profile data asynchronously using LaunchedEffect
//    LaunchedEffect(email) {
//        try {
//            // Fetch the user profile from the database in a coroutine
//            val profile = userProfileDao.getUserByEmail(email)
//            userProfile = profile
//            isLoading = false
//        } catch (e: Exception) {
//            errorMessage = "Failed to load profile: ${e.message}"
//            isLoading = false
//        }
//    }
//
//    // UI to display the profile data
//    if (isLoading) {
//        // Show a loading indicator while data is being fetched
//        CircularProgressIndicator()
//    } else {
//        if (userProfile != null) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)
//            ) {
//                Text("Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold)
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text("Email: ${userProfile?.email}")
//                Text("First Name: ${userProfile?.firstName}")
//                Text("Last Name: ${userProfile?.lastName}")
//                Text("Contact Info: ${userProfile?.contact ?: "N/A"}")
//            }
//        } else {
//            Text("User profile not found.")
//        }
//
//        // Show error message if any
//        if (errorMessage.isNotEmpty()) {
//            Text(
//                text = errorMessage,
//                color = Color.Red,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        }
//    }
//}



//PROFILE EDITING
@Composable
fun ProfileScreen(
    email: String,
    userProfileDao: UserProfileDao,
    profileViewModel: UserProfileViewModel,
    navController: NavHostController,
) {

    // State to hold the user profile data
////    val userProfileState by profileViewModel.userProfile.collectAsState()
////    val isLoading by profileViewModel.isLoading.collectAsState()
////    val errorMessage by profileViewModel.errorMessage.collectAsState()

    // State to hold the user profile data
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    // Load the user profile when the screen is shown
    LaunchedEffect(email) {
        try {
            // Fetch the user profile from the database in a coroutine
            val profile = userProfileDao.getUserByEmail(email)
            userProfile = profile
            isLoading = false
        } catch (e: Exception) {
            errorMessage = "Failed to load profile: ${e.message}"
            isLoading = false
        }
    }

    // UI to display the profile data
    if (isLoading) {
        CircularProgressIndicator()
    } else {
        if (userProfile != null) {
            var firstName by remember { mutableStateOf(userProfile!!.firstName) }
            var lastName by remember { mutableStateOf(userProfile!!.lastName) }
            var contact by remember { mutableStateOf(userProfile!!.contact ?: "") }
            var city by remember { mutableStateOf(userProfile!!.city ?: "") }
            var region by remember { mutableStateOf(userProfile!!.region ?: "") }
            var postalCode by remember { mutableStateOf(userProfile!!.postalCode ?: "") }
            val overallRating = userProfile!!.rating ?: "No ratings yet"

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Edit Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                Text("Email: ${userProfile!!.email}")
                Spacer(modifier = Modifier.height(8.dp))

                // Editable fields
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = contact,
                    onValueChange = { contact = it },
                    label = { Text("Contact Info") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = region,
                    onValueChange = { region = it },
                    label = { Text("Region") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = postalCode,
                    onValueChange = { postalCode = it },
                    label = { Text("Postal Code") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text("Overall Rating: $overallRating", fontSize = 16.sp)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        profileViewModel.updateUserrProfile(
                            email = email,
                            firstName = firstName,
                            lastName = lastName,
                            contact = contact,
                            city = city,
                            region = region,
                            postalCode = postalCode
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }
                Button(
                    onClick = { navController.navigate("property_screen/${email}")}

                ) {
                    Text("Property")
                }


            }
        } else {
            Text("User profile not found.")
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

