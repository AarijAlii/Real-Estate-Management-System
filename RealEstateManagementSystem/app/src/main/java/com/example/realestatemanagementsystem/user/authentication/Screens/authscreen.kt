//package com.example.realestatemanagementsystem.user.authentication.Screens
//
//import androidx.compose.material3.OutlinedTextField
//import android.widget.Toast
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonColors
//import androidx.compose.material3.Card
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.compose.runtime.setValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.FocusDirection
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.example.realestatemanagementsystem.Navigation.Screen
//import com.example.realestatemanagementsystem.R
//import com.example.realestatemanagementsystem.AppDatabase
//import com.example.realestatemanagementsystem.user.authentication.AuthenticationViewModel
//import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthState
//import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
//
//
//@Composable
//fun AuthenticationScreen(viewModel: AuthenticationViewModel) {
//    val email = remember { mutableStateOf("") }
//    val password = remember { mutableStateOf("") }
//    val signUpResult by viewModel.signUpResult.observeAsState()
//    val signInResult by viewModel.signInResult.observeAsState()
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("Sign Up / Sign In", style = MaterialTheme.typography.h5)
//
//        TextField(
//            value = email.value,
//            onValueChange = { email.value = it },
//            label = { Text("Email") },
//            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
//        )
//
//        TextField(
//            value = password.value,
//            onValueChange = { password.value = it },
//            label = { Text("Password") },
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = { viewModel.signUp(email.value, password.value) },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Sign Up")
//        }
//
//        Button(
//            onClick = { viewModel.signIn(email.value, password.value) },
//            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
//        ) {
//            Text("Sign In")
//        }
//
//        signUpResult?.let {
//            Text(it, color = Color.Green, modifier = Modifier.padding(top = 16.dp))
//        }
//
//        signInResult?.let {
//            Text(it, color = Color.Green, modifier = Modifier.padding(top = 16.dp))
//        }
//    }
//}
