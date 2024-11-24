//package com.example.realestatemanagementsystem.user.UserProfile.Screens
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonColors
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.FocusDirection
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.example.realestatemanagementsystem.Navigation.Screen
//import com.example.realestatemanagementsystem.user.UserProfile.UserProfile
//import com.example.realestatemanagementsystem.user.UserProfile.UserProfileDao
//import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
//
////@Composable
////fun UserProfileUpdateScreen(
////    email: String,
////    userProfileDao: UserProfileDao,
////    profileViewModel: UserProfileViewModel,
////    navHostController: NavHostController
////) {
////    // State variables for the user input
////    val focusManager= LocalFocusManager.current
////
////    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
////    var isLoading by remember { mutableStateOf(true) }
////    var errorMessage by remember { mutableStateOf("") }
////    LaunchedEffect(email) {
////        try {
////            // Fetch the user profile from the database in a coroutine
////            val profile = userProfileDao.getUserByEmail(email)
////            userProfile = profile
////            isLoading = false
////        } catch (e: Exception) {
////            errorMessage = "Failed to load profile: ${e.message}"
////            isLoading = false
////        }
////    }
////    if (isLoading) {
////        CircularProgressIndicator()
////    } else {
////        if (userProfile != null) {
////            var firstName by remember { mutableStateOf(userProfile?.firstName?:"") }
////            var lastName by remember { mutableStateOf(userProfile?.lastName?:"") }
////            var contact by remember { mutableStateOf(userProfile?.contact ?: "") }
////            var city by remember { mutableStateOf(userProfile?.city ?: "") }
////            var region by remember { mutableStateOf(userProfile?.region ?: "") }
////            var postalCode by remember { mutableStateOf(userProfile?.postalCode ?: "") }
////            val overallRating = userProfile?.rating ?: "No ratings yet"
////            Column(verticalArrangement = Arrangement.Center,modifier = Modifier
////
////                .padding(16.dp)) {
////
////                Text("Update Profile", fontSize = 34.sp, fontWeight = FontWeight.Bold,modifier=Modifier.padding(vertical = 16.dp))
////
////                Column(
////
////                    verticalArrangement = spacedBy(16.dp)
////                ) {
////                    TextField(
////                        value = firstName,
////                        onValueChange = { firstName = it },
////                        label = { Text("First Name") },
////                        modifier = Modifier.fillMaxWidth(),
////                        keyboardOptions = KeyboardOptions(
////                            keyboardType = KeyboardType.Text,
////                            imeAction = ImeAction.Next
////                        ),
////                        keyboardActions = KeyboardActions(
////                            onNext = {
////                                focusManager.moveFocus(FocusDirection.Down)
////                            })
////
////                    )
////
////                    TextField(
////                        value = lastName,
////                        onValueChange = { lastName = it },
////                        label = { Text("Last Name") },
////                        modifier = Modifier.fillMaxWidth(),
////                        keyboardOptions = KeyboardOptions(
////                            keyboardType = KeyboardType.Text,
////                            imeAction = ImeAction.Next
////                        ),
////                        keyboardActions = KeyboardActions(
////                            onNext = {
////                                focusManager.moveFocus(FocusDirection.Down)
////                            })
////                    )
////
////                    TextField(
////                        value = contact,
////                        onValueChange = { contact = it },
////                        label = { Text("Contact") },
////
////                        modifier = Modifier.fillMaxWidth(),
////                        keyboardOptions = KeyboardOptions(
////                            keyboardType = KeyboardType.Phone,
////                            imeAction = ImeAction.Next
////                        ),
////                        keyboardActions = KeyboardActions(
////                            onNext = {
////                                focusManager.moveFocus(FocusDirection.Down)
////                            })
////                    )
////
////                    TextField(
////                        value = city,
////                        onValueChange = { city = it },
////                        label = { Text("City") },
////                        modifier = Modifier.fillMaxWidth(),
////                        keyboardOptions = KeyboardOptions(
////                            keyboardType = KeyboardType.Text,
////                            imeAction = ImeAction.Next
////                        ),
////                        keyboardActions = KeyboardActions(
////                            onNext = {
////                                focusManager.moveFocus(FocusDirection.Down)
////                            })
////                    )
////
////                    TextField(
////                        value = region,
////                        onValueChange = { region = it },
////                        label = { Text("Region") },
////                        modifier = Modifier.fillMaxWidth(),
////                        keyboardOptions = KeyboardOptions(
////                            keyboardType = KeyboardType.Text,
////                            imeAction = ImeAction.Next
////                        ),
////                        keyboardActions = KeyboardActions(
////                            onNext = {
////                                focusManager.moveFocus(FocusDirection.Down)
////                            })
////                    )
////
////                    TextField(
////                        value = postalCode,
////                        onValueChange = { postalCode = it },
////                        label = { Text("Postal Code") },
////
////                        modifier = Modifier.fillMaxWidth(),
////                        keyboardOptions = KeyboardOptions(
////                            keyboardType = KeyboardType.Number,
////                            imeAction = ImeAction.Done
////                        ),
////                        keyboardActions = KeyboardActions(
////                            onNext = {
////                                focusManager.moveFocus(FocusDirection.Down)
////                            })
////
////
////                    )
////                      val useerProfile = UserProfile(
////                          email = email,
////                          firstName = firstName,
////                          lastName = lastName,
////                          contact = contact,
////                          city = city,
////                          region = region,
////                          postalCode = postalCode,
////                          rating = 2
////                      )
////                    Button(
////                        onClick = {
////                            // Create a UserProfile instance
////                          profileViewModel.updateUserProfileOnApi(email,useerProfile)
////
////                            navHostController.navigate("home_screen/$email")// Save the user profile using the ViewModel
////
////                        },
////                        colors = ButtonColors(
////                            contentColor = Color.White,
////                            disabledContainerColor = Color.Gray,
////                            containerColor = Color.Red,
////                            disabledContentColor = Color.White,
////                        ) ,
////                        modifier = Modifier.fillMaxWidth()
////
////                    ) {
////                        Text("Save")
////                    }
////                }
////            }
////
////        } else {
////            Text("User profile not found.")
////        }
////
////        if (errorMessage.isNotEmpty()) {
////            Text(
////                text = errorMessage,
////                color = Color.Red,
////                modifier = Modifier.padding(top = 8.dp)
////            )
////        }
////    }
////}
//
//
//@Composable
//fun UserProfileUpdateScreen(
//
//    email: String,
//    userProfileDao: UserProfileDao,
//    profileViewModel: UserProfileViewModel,
//    navHostController: NavHostController
//) {
//    var userProfile by remember { mutableStateOf(UserProfile(email)) }
//    val userProfileState = profileViewModel.userProfile.collectAsState()
//    val errorMessage by profileViewModel.errorMessage.observeAsState()
//
//    // Fetch user profile when the screen is loaded
//    LaunchedEffect(email) {
//        profileViewModel.getUserProfileFromApi(email)
//    }
//
//    // Update UI when the user profile is updated
//    userProfileState.value?.let {
//        userProfile = it
//    }
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("User Profile", style = MaterialTheme.typography.h4)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Display profile information or fields to edit
//        UserProfileField(label = "First Name", value = userProfile.firstName) {
//            userProfile = userProfile.copy(firstName = it)
//        }
//        UserProfileField(label = "Last Name", value = userProfile.lastName) {
//            userProfile = userProfile.copy(lastName = it)
//        }
//        UserProfileField(label = "Contact", value = userProfile.contact) {
//            userProfile = userProfile.copy(contact = it)
//        }
//        UserProfileField(label = "City", value = userProfile.city) {
//            userProfile = userProfile.copy(city = it)
//        }
//
//        UserProfileField(label = "City", value = userProfile.region) {
//            userProfile = userProfile.copy(region = it)
//        }
//        UserProfileField(label = "City", value = userProfile.postalCode) {
//            userProfile = userProfile.copy(postalCode = it)
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Show error message if there is an issue
//
//
//        // Buttons for actions (Create, Update, Fetch)
//        Button(onClick = {
//            profileViewModel.createUserProfile(userProfile)
//        }) {
//            Text("Create Profile")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Button(onClick = {
//            profileViewModel.updateUserProfilee(email, userProfile)
//        }) {
//            Text("Update Profile")
//        }
//    }
//}
//
//@Composable
//fun UserProfileField(label: String, value: String, onValueChange: (String) -> Unit) {
//    Text(label)
//    Spacer(modifier = Modifier.height(4.dp))
//    BasicTextField(
//        value = TextFieldValue(value),
//        onValueChange = { onValueChange(it.text) },
//        modifier = Modifier.fillMaxWidth(),
//        singleLine = true
//    )
//}
//
