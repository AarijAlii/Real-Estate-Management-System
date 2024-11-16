package com.example.realestatemanagementsystem.user.authentication.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.realestatemanagementsystem.user.UserProfile.AppDatabase
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthState
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel

//@Composable
//fun SignUpScreen(
//    authViewModel: AuthViewModel = viewModel(),
//    navHostController: NavHostController
//) {
//    val focusManager= LocalFocusManager.current
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var passwordVisible by remember { mutableStateOf(false) }
//    val context = LocalContext.current
//    val authState = authViewModel.authState.observeAsState()
//    LaunchedEffect(authState.value) {
//        when (authState.value) {
//            is AuthState.Success -> {
//               navHostController.navigate(route= Screen.HomeScreen.route)
//            }
//
//            is AuthState.Error -> {
//                Toast.makeText(
//                    context,
//                    (authState.value as AuthState.Error).message,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            else -> Unit
//        }
//    }
//    Surface(modifier = Modifier
//        .fillMaxSize(),
//         color = Color.Red
//
//
//    ) {
//
//        Column(modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.SpaceEvenly
//                ) {
//                Text("Property Hub", color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.SansSerif)
//            Column(
//                modifier = Modifier
//
//                    .padding(16.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//
//                Card() {
//
//                    Column(
//                        modifier = Modifier
//                            .padding(16.dp),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text(
//                            text = "Create Account",
//                            fontSize = 22.sp,
//                            fontWeight = FontWeight.ExtraBold,
//                            modifier = Modifier.padding(horizontal = 26.dp, vertical = 16.dp)
//                        )
//                        OutlinedTextField(
//                            keyboardOptions = KeyboardOptions(
//                                keyboardType = KeyboardType.Email,
//                                imeAction = ImeAction.Next
//                            ),
//                            keyboardActions = KeyboardActions(
//                                onNext = {
//                                    focusManager.moveFocus(FocusDirection.Down)
//                                }),
//                            value = email,
//                            onValueChange = { email = it },
//                            label = { Text("Email") },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(8.dp)
//                        )
//                        OutlinedTextField(
//                            keyboardOptions = KeyboardOptions(
//                                keyboardType = KeyboardType.Password,
//                                imeAction = ImeAction.Next
//                            ),
//                            keyboardActions = KeyboardActions(
//                                onNext = {
//                                    focusManager.moveFocus(FocusDirection.Down)
//                                }),
//                            value = password,
//                            onValueChange = { password = it },
//                            label = { Text("Password") },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(8.dp),
//                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                            trailingIcon = {
//                                val image = if (passwordVisible)
//                                    painterResource(id = R.drawable.baseline_visibility_24)
//                                else
//                                    painterResource(id = R.drawable.baseline_visibility_off_24)
//
//                                // IconButton to toggle password visibility
//                                IconButton(onClick = {
//                                    passwordVisible = !passwordVisible
//                                }) {
//                                    Icon(painter=image, contentDescription = "show pass")
//                                }
//                            }
//                        )
//                        OutlinedTextField(
//                            keyboardOptions = KeyboardOptions(
//                                keyboardType = KeyboardType.Password,
//                                imeAction = ImeAction.Done
//                            ),
//                            keyboardActions = KeyboardActions(
//                                onDone = {
//                                    authViewModel.signUp(email, password)
//                                    email = ""
//                                    password = ""
//                                }),
//                            value = password,
//                            onValueChange = { password = it },
//                            label = { Text("Confirm Password") },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(8.dp),
//                            visualTransformation = PasswordVisualTransformation()
//                        )
//                        Button(
//                            onClick = {
//                                authViewModel.signUp(email, password)
//                                navHostController.navigate(Screen.UserProfileScreen.route)
//                                //              Toast.makeText(context, "Signed Up Successfully", Toast.LENGTH_LONG).show()
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(8.dp),
//                            colors = ButtonColors(
//                                contentColor = Color.White,
//                                disabledContainerColor = Color.Gray,
//                                containerColor = Color.Red,
//                                disabledContentColor = Color.White,
//                            ),
//                            enabled = authState.value != AuthState.Loading
//                        ) {
//                            Text("Sign Up")
//                        }
//
//                        Row {
//                            Spacer(modifier = Modifier.height(16.dp))
//
//                            Text("Already have an account? ",
//                                modifier = Modifier.clickable {
//                                    navHostController.navigate(route = Screen.LoginScreen.route)
//                                }
//
//                            )
//                            Text(
//                                text = "Sign in.", modifier = Modifier
//                                    .clickable { navHostController.navigate(route = Screen.LoginScreen.route)},
//                                color = Color.Red
//                            )
//                        }
//
//                    }
//                }
//            }
//        }
//    }
//    }


@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    navController: NavHostController,
    appDatabase: AppDatabase,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var contactInfo by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }


    // Profile data object
    val userProfile = UserProfile(
        email = email,
        firstName = firstName,
        lastName = lastName,
        contact = contactInfo
    )

    // UI for the sign-up screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Sign Up", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = email.isEmpty()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = password.isEmpty()
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = confirmPassword.isEmpty() || confirmPassword != password
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = contactInfo,
            onValueChange = { contactInfo = it },
            label = { Text("Contact Info") },
            modifier = Modifier.fillMaxWidth()
        )

        // Error message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,

                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = {

                authViewModel.signUp(
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    userProfile = userProfile,
                    appDatabase = appDatabase// Pass appDatabase here
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = email.isNotEmpty() && password.isNotEmpty() && confirmPassword == password
        ) {
            Text("Sign Up")
        }

        // Observe the authState with collectAsState()
        val authState by authViewModel.authState.collectAsState()

        when (authState) {
            is AuthState.Success -> {
                // Navigate to the Profile screen after successful sign-up
                navController.navigate("profile_screen/${email}")
            }
            is AuthState.Error -> {
                errorMessage = (authState as AuthState.Error).message ?: "Sign up failed."
            }
            else -> {}
        }
    }
}