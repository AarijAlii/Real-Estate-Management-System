package com.example.realestatemanagementsystem.user.authentication.Screens
//
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.realestatemanagementsystem.Navigation.Screen
import com.example.realestatemanagementsystem.R
import com.example.realestatemanagementsystem.AppDatabase
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthState
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    navHostController: NavHostController,
    appDatabase: AppDatabase,
    firebaseFirestore: FirebaseFirestore
) {
    val focusManager = LocalFocusManager.current
    var email by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var contactInfo by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val authState = authViewModel.authState.collectAsState()
    val userProfile = UserProfile(
        email = email,
        firstName = firstName,
        lastName = lastName,
        contact = contactInfo
    )

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Success -> {
                navHostController.navigate("profile_screen/${email}")
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.Red) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
            Text("Property Hub", color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.SansSerif)
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Card {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Create Account", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(horizontal = 26.dp, vertical = 16.dp))
                        OutlinedTextField(
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )
                        OutlinedTextField(
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val image = if (passwordVisible) painterResource(id = R.drawable.baseline_visibility_24) else painterResource(id = R.drawable.baseline_visibility_off_24)
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(painter = image, contentDescription = "show pass")
                                }
                            }
                        )
                        OutlinedTextField(
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                if (password == confirmPassword) {
                                    authViewModel.signUp(
                                        email = email,
                                        password = password,
                                        confirmPassword = confirmPassword,
                                        userProfile = userProfile,
                                        appDatabase = appDatabase,
                                        firebaseFirestore = firebaseFirestore
                                    )
                                    navHostController.navigate("profile_screen/${email}")
                                } else {
                                    errorMessage = "Passwords do not match"
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                                }
                            }),
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirm Password") },
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            visualTransformation = PasswordVisualTransformation(),
                            trailingIcon = {
                                val image = if (confirmPasswordVisible) painterResource(id = R.drawable.baseline_visibility_24) else painterResource(id = R.drawable.baseline_visibility_off_24)
                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                    Icon(painter = image, contentDescription = "show pass")
                                }
                            }
                        )
                        Button(
                            onClick = {
                                if (password == confirmPassword) {
                                    authViewModel.signUp(
                                        email = email,
                                        password = password,
                                        confirmPassword = confirmPassword,
                                        userProfile = userProfile,
                                        appDatabase = appDatabase,
                                        firebaseFirestore = firebaseFirestore
                                    )
                                    navHostController.navigate("profile_screen/${email}")
                                } else {
                                    errorMessage = "Passwords do not match"
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Text("Sign Up")
                        }
                    }
                }
            }
        }
    }
}


//@Composable
//fun SignUpScreen(
//    authViewModel: AuthViewModel,
//    navHostController: NavHostController,
//    appDatabase: AppDatabase,
//    firebaseFirestore: FirebaseFirestore
//) {
//
//    val focusManager= LocalFocusManager.current
//    var email by remember { mutableStateOf("") }
//    var confirmPassword by remember{ mutableStateOf("")}
//    var password by remember { mutableStateOf("") }
//    var passwordVisible by remember { mutableStateOf(false) }
//    var confirmPasswordVisible by remember { mutableStateOf(false) }
//    var firstName by remember { mutableStateOf("") }
//    var lastName by remember { mutableStateOf("") }
//    var contactInfo by remember { mutableStateOf("") }
//    var errorMessage by remember { mutableStateOf("") }
//    val context = LocalContext.current
//    val authState = authViewModel.authState.collectAsState()
//    val userProfile = UserProfile(
//        email = email,
//        firstName = firstName,
//        lastName = lastName,
//        contact = contactInfo
//    )
//    LaunchedEffect(authState.value) {
//        when (authState.value) {
//            is AuthState.Success -> {
//               navHostController.navigate("profile_screen/${email}")
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
//                                    if(password==confirmPassword){
//                                        authViewModel.signUp(
//                                            email = email,
//                                            password = password,
//                                            confirmPassword = confirmPassword,
//                                            userProfile = userProfile,
//                                            appDatabase = appDatabase,// Pass appDatabase here
//                                            firebaseFirestore =  firebaseFirestore
//                                        )
//                                        navHostController.navigate("profile_screen/${email}")}
//                                    else {
//                                        errorMessage = "Passwords do not match"
//                                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
//                                    }
//                                }),
//                            value = confirmPassword,
//                            onValueChange = { confirmPassword = it },
//                            label = { Text("Confirm Password") },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(8.dp),
//                            visualTransformation = PasswordVisualTransformation(),
//                            trailingIcon = {
//                                val image = if (confirmPasswordVisible)
//                                    painterResource(id = R.drawable.baseline_visibility_24)
//                                else
//                                    painterResource(id = R.drawable.baseline_visibility_off_24)
//
//                                // IconButton to toggle password visibility
//                                IconButton(onClick = {
//                                    confirmPasswordVisible = !confirmPasswordVisible
//                                }) {
//                                    Icon(painter=image, contentDescription = "show pass")
//                                }
//                            }
//                        )
//                        Button(
//                            onClick = {
//                                if(password==confirmPassword){
//                                authViewModel.signUp(
//                                    email = email,
//                                    password = password,
//                                    confirmPassword = confirmPassword,
//                                    userProfile = userProfile,
//                                    appDatabase = appDatabase,
//                                    firebaseFirestore =  firebaseFirestore
//                                // Pass appDatabase here
//                                )
//
//                                navHostController.navigate("profile_screen/${email}")}
//                                      else {
//                                            errorMessage = "Passwords do not match"
//                                           Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
//                                }
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
//
