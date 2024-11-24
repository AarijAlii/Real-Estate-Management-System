package com.example.realestatemanagementsystem.user.authentication.Screens


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
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.realestatemanagementsystem.Navigation.Screen
import com.example.realestatemanagementsystem.R
import com.example.realestatemanagementsystem.AppDatabase
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile
import com.example.realestatemanagementsystem.user.UserProfile.UuserProfile
import com.example.realestatemanagementsystem.user.authentication.AuthenticationViewModel
import kotlinx.coroutines.delay

@Composable
fun SignnUpScreen(authViewModel: AuthenticationViewModel, navController: NavController) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val contact = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val region = remember { mutableStateOf("") }
    val postalCode = remember { mutableStateOf("") }
    val rating = remember { mutableStateOf(0) }
    val signUpResult by authViewModel.signUpResult.observeAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Sign Up", fontSize = 24.sp)

        OutlinedTextField(value = email.value, onValueChange = { email.value = it }, label = { Text("Email") })
        OutlinedTextField(value = password.value, onValueChange = { password.value = it }, label = { Text("Password") })
        OutlinedTextField(value = firstName.value, onValueChange = { firstName.value = it }, label = { Text("First Name") })
        OutlinedTextField(value = lastName.value, onValueChange = { lastName.value = it }, label = { Text("Last Name") })
        OutlinedTextField(value = contact.value, onValueChange = { contact.value = it }, label = { Text("Contact") })
        OutlinedTextField(value = city.value, onValueChange = { city.value = it }, label = { Text("City") })
        OutlinedTextField(value = region.value, onValueChange = { region.value = it }, label = { Text("Region") })
        OutlinedTextField(value = postalCode.value, onValueChange = { postalCode.value = it }, label = { Text("Postal Code") })
        OutlinedTextField(value = rating.value.toString(), onValueChange = { rating.value = it.toIntOrNull() ?: 0 }, label = { Text("Rating") })

        Button(
            onClick = {
                val user = UuserProfile(
                    email.value, password.value, firstName.value, lastName.value,
                    contact.value, city.value, region.value,postalCode.value,rating.value
                )
                authViewModel.signUp(user)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Sign Up")
        }

        signUpResult?.let {
            Text(it)
            if (it.contains("User registered successfully")) {
                LaunchedEffect(true) {
                    delay(2000)
                    navController.navigate(Screeen.SignInnScreen.route)
                }
            }
        }
    }
}
