package com.example.realestatemanagementsystem.user.authentication


import androidx.compose.material3.OutlinedTextField
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    //val result by authViewModel.authState.observeAsState()
    val focusManager= LocalFocusManager.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Success -> {
                onNavigateToHome()
            }

            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState.value as AuthState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> Unit
        }
    }

    Surface(modifier = Modifier
        .fillMaxSize(),
        color = Color.Red


    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(
                state = rememberScrollState()
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("Property Hub", color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.SansSerif)
            Column(
                modifier = Modifier

                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Card() {

                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Welcome Back",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 26.dp, vertical = 16.dp)
                        )
                        OutlinedTextField(
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }),

                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        OutlinedTextField(
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone  = {
                                    authViewModel.login(email, password)
                                }),
//            colors = TextFieldDefaults.textFieldColors(
//                focusedBorderColor = Color.Red
//            ),

                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)

                            ,
                            visualTransformation = PasswordVisualTransformation())
        Button(
            onClick = {
                authViewModel.login(email, password)
//                when (result) {
//                    is Result.Success->{
//                        onNavigateToSuccess()
//                    }
//                    is Result.Error ->{
//                        Toast.makeText(context, "Login failed", Toast.LENGTH_LONG).show()
//                    }
//                    else -> {
//                    }
//                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

            , colors = ButtonColors(
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                containerColor = Color.Red,
                disabledContentColor = Color.White,
            ),


            enabled = authState.value != AuthState.Loading
        ) {
            Text("Login")
        }
        Row {
            Spacer(modifier = Modifier.height(16.dp))

            Text("Don't have an account? ",
                modifier = Modifier.clickable {
                    onNavigateToSignUp()
                }

            )
            Text(text= "Sign up.",modifier=Modifier
                .clickable { onNavigateToSignUp() },
                color = Color.Red)}
    }
}
            }
        }
    }
}

