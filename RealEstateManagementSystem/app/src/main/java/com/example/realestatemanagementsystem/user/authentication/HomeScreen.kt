package com.example.realestatemanagementsystem.user.authentication

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

@Composable
fun HomeScreen(authViewModel: AuthViewModel = viewModel(), OnNavigateToLogin: () -> Unit = {})
{
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value){
        if (authState.value == AuthState.Failed) {
            OnNavigateToLogin()
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