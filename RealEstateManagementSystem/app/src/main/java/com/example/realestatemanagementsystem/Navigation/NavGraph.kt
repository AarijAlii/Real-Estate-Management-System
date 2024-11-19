package com.example.realestatemanagementsystem.Navigation

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.test.core.app.ApplicationProvider.getApplicationContext

import com.example.realestatemanagementsystem.Home.Screens.CreateListingScreen
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
import com.example.realestatemanagementsystem.Home.Screens.HomeScreen
import com.example.realestatemanagementsystem.Home.Screens.SellScreen
import com.example.realestatemanagementsystem.Property.PropertyViewModel
import com.example.realestatemanagementsystem.Property.PropertyViewModelFactory
import com.example.realestatemanagementsystem.user.UserProfile.AppDatabase
import com.example.realestatemanagementsystem.user.UserProfile.Screens.UserProfileScreen
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import com.example.realestatemanagementsystem.user.UserProfile.UserViewModelFactory
import com.example.realestatemanagementsystem.user.authentication.Screens.LoginScreen
import com.example.realestatemanagementsystem.user.authentication.Screens.SignUpScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {
        composable(Screen.SignupScreen.route) {
            SignUpScreen(
                authViewModel = viewModel(),
                navHostController =  navController,
                appDatabase = AppDatabase.getDatabase(LocalContext.current) // Pass the appDatabase here
            )
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel = viewModel(),
                navHostController = navController,
                appDatabase = AppDatabase.getDatabase(LocalContext.current)
            )
        }
        composable(Screen.HomeScreen.route) {
                backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            val context = LocalContext.current
            val appDatabase = AppDatabase.getDatabase(context)
            val factory = UserViewModelFactory(appDatabase)  // Pass AppDatabase here
            val userProfileViewModel: UserProfileViewModel = viewModel(factory = factory)

            if (email != null) {
                Log.d("HomeScreen", "Email received: $email")
                HomeScreen(

                    email = email,
                    authViewModel = AuthViewModel(),
                    navHostController = navController,
                    userProfileDao = appDatabase.userProfileDao(),
                    profileViewModel = userProfileViewModel
                )
            }
        }

        composable(Screen.SellScreen.route){
                backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            val context = LocalContext.current
            val appDatabase = AppDatabase.getDatabase(context)
            val userFactory = UserViewModelFactory(appDatabase)  // Pass AppDatabase here
            val userProfileViewModel: UserProfileViewModel = viewModel(factory = userFactory)
            val propertyFactory = PropertyViewModelFactory(appDatabase.propertyDao())
            val propertyViewModel: PropertyViewModel = viewModel(factory = propertyFactory)

            if (email != null) {
               SellScreen(
                    email = email,
                    userProfileDao = appDatabase.userProfileDao(),
                    profileViewModel = userProfileViewModel,
                    navHostController = navController,
                   authViewModel = AuthViewModel(),
                   propertyViewModel = propertyViewModel
                )
            }
        }
        composable(Screen.UserProfileScreen.route) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            val context = LocalContext.current
            val appDatabase = AppDatabase.getDatabase(context)
            val factory = UserViewModelFactory(appDatabase)  // Pass AppDatabase here
            val userProfileViewModel: UserProfileViewModel = viewModel(factory = factory)

            if (email != null) {
                UserProfileScreen(
                    email = email,
                    userProfileDao = appDatabase.userProfileDao(),
                    profileViewModel = userProfileViewModel,
                    navHostController = navController
                )
            }
        }
        composable(Screen.CreateListingScreen.route){
                backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            val context = LocalContext.current
            val appDatabase = AppDatabase.getDatabase(context)
            val factory = PropertyViewModelFactory(appDatabase.propertyDao())
            val propertyViewModel: PropertyViewModel = viewModel(factory = factory)
            if (email != null) {
                CreateListingScreen(email = email, navController = navController, propertyViewModel = propertyViewModel)
            }
        }
    }
}