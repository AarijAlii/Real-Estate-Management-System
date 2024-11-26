package com.example.realestatemanagementsystem.user.UserProfile.Screens


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileDao
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun UserProfileScreen(
    email:String,
    profileViewModel: UserProfileViewModel,
    navHostController: NavHostController,
    userProfileDao: UserProfileDao
) {
    // State variables for the user input
    val focusManager = LocalFocusManager.current

    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scope=rememberCoroutineScope()
    val context = LocalContext.current
    val clientId = "68edc80df54e62f"

    var imageUris by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    // Image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent() // Single content (photo)
    ) { uri: Uri? ->
        // Handle the selected single image URI
        imageUris = uri
    }


    LaunchedEffect(email) {
        try {
            // Fetch the user profile from the database in a coroutine
           while(userProfile==null){
               val profile = userProfileDao.getUserByEmail(email)
                userProfile = profile
                isLoading = false}
        } catch (e: Exception) {
            errorMessage = "Failed to load profile: ${e.message}"
            isLoading = false
        }
    }

    if (isLoading){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Row{
            CircularProgressIndicator(color = Color.Red,)
            Text("Loading")}
        }
    }
    else{
        if (userProfile != null) {
            var firstName by remember { mutableStateOf(userProfile?.firstName ?: "") }
            var lastName by remember { mutableStateOf(userProfile?.lastName ?: "") }
            var contact by remember { mutableStateOf(userProfile?.contact ?: "") }
            var city by remember { mutableStateOf(userProfile?.city ?: "") }
            var region by remember { mutableStateOf(userProfile?.region ?: "") }
            var postalCode by remember { mutableStateOf(userProfile?.postalCode ?: "") }
            //val overallRating = userProfile?.rating ?: "No ratings yet"
            Column(
                verticalArrangement = Arrangement.Center, modifier = Modifier

                    .padding(16.dp)
            ) {

                Text(
                    "Create Profile",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Column(

                    verticalArrangement = spacedBy(16.dp)
                ) {
                    TextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First Name") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            })

                    )

                    TextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last Name") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            })
                    )

                    TextField(
                        value = contact,
                        onValueChange = { contact = it },
                        label = { Text("Contact") },

                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            })
                    )

                    TextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            })
                    )

                    TextField(
                        value = region,
                        onValueChange = { region = it },
                        label = { Text("Region") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            })
                    )

                    TextField(
                        value = postalCode,
                        onValueChange = { postalCode = it },
                        label = { Text("Postal Code") },

                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            })


                    )
                    if (imageUris!=null) {

                        val painter = rememberAsyncImagePainter(imageUris)
                        Image(
                            painter = painter,
                            contentDescription = "Selected image",
                            modifier = Modifier
                                .padding(8.dp)
                                .size(100.dp), // Adjust size as needed
                            contentScale = ContentScale.Crop
                        )


                    }
                    Button(
                        onClick = {
                            // Create a UserProfile instance

                            val userProfile = UserProfile(
                                email = email,
                                firstName = firstName,
                                lastName = lastName,
                                contact = contact,
                                city = city,
                                region = region,
                                postalCode = postalCode
                                //rating=0,

                            )
                            if (imageUris!=null) {
                                // Launch coroutine for suspend function

                                    profileViewModel.saveUserProfile(userProfile, imageUris, context, clientId)
                                    navHostController.navigate("home_screen/$email")

                            } else {
                                errorMessage = "Please select at least one image."
                            }// Save the user profile using the ViewModel

                        },
                        colors = ButtonColors(
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            containerColor = Color.Red,
                            disabledContentColor = Color.White,
                        ),
                        modifier = Modifier.fillMaxWidth()

                    ) {
                        Text("Create Profile")
                    }
                    Button(onClick = {imagePickerLauncher.launch("image/*")},modifier=Modifier.fillMaxWidth() , colors = ButtonColors(
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        containerColor = Color.Red,
                        disabledContentColor = Color.White,
                    )){
                        Text(text = "Add Photo")
                    }
                }
            }

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


