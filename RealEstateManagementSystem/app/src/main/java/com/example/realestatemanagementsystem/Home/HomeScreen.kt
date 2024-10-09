package com.example.realestatemanagementsystem.Home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.realestatemanagementsystem.NavGraph.Screen
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthState
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel

@Composable
fun HomeScreen(authViewModel: AuthViewModel = viewModel(), navHostController: NavHostController)
{
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value){
        if (authState.value == AuthState.Failed) {
            navHostController.navigate(route = Screen.LoginScreen.route)
        }
    }

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Success")
        Button(onClick = {
            authViewModel.signOut() })
        {
            Text(text = "Sign Out")
        }
    }

}
