package com.example.realestatemanagementsystem.NavGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
import com.example.realestatemanagementsystem.Home.HomeScreen
import com.example.realestatemanagementsystem.user.authentication.Screens.LoginScreen
import com.example.realestatemanagementsystem.user.authentication.Screens.SignUpScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignupScreen.route
    ) {
        composable(Screen.SignupScreen.route) {
            SignUpScreen(
                authViewModel,
                navHostController=navController
            )
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel,
              navHostController = navController
            )
        }
        composable(Screen.HomeScreen.route) {
            HomeScreen(
                authViewModel,
                navHostController=navController
            )
        }
    }
}