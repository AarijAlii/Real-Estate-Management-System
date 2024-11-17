package com.example.realestatemanagementsystem.Navigation

import android.app.Application
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.realestatemanagementsystem.Home.Screens.BuyScreen
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
import com.example.realestatemanagementsystem.Home.Screens.HomeScreen
import com.example.realestatemanagementsystem.Home.Screens.SellScreen
import com.example.realestatemanagementsystem.user.UserProfile.Screens.UserProfileScreen
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
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
        composable(Screen.BuyScreen.route){
            BuyScreen(
                modifier=Modifier,
                navHostController = navController,
                innerPadding = PaddingValues()
            )
        }
        composable(Screen.SellScreen.route){
            SellScreen(
                modifier=Modifier,
                navHostController = navController
            )
        }
        composable(Screen.UserProfileScreen.route){
            UserProfileScreen(
                UserProfileViewModel(application = Application())
            )
        }
    }
}