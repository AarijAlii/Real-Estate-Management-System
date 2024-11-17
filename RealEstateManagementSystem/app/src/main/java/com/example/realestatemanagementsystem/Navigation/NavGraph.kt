package com.example.realestatemanagementsystem.Navigation

import android.app.Application
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.realestatemanagementsystem.Home.Screens.BuyScreen
import com.example.realestatemanagementsystem.Home.Screens.CreateListingScreen
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
    authViewModel: AuthViewModel,
    userProfileViewModel: UserProfileViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {
        composable(Screen.SignupScreen.route) {
            SignUpScreen(
                authViewModel,
                navHostController=navController
            )
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel=AuthViewModel(),
              navHostController = navController
            )
        }
        composable(Screen.HomeScreen.route) {
            HomeScreen(
                authViewModel= AuthViewModel(),
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
                authViewModel = AuthViewModel(),
                modifier=Modifier,
                navHostController = navController
            )
        }
        composable(Screen.UserProfileScreen.route){
            UserProfileScreen(
                userProfileViewModel
            )
        }
        composable(Screen.CreateListingScreen.route){
           CreateListingScreen(
                modifier=Modifier,
                navHostController = navController
            )
        }
    }
}