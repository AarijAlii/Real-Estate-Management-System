package com.example.realestatemanagementsystem.Navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.realestatemanagementsystem.Home.Screens.BuyScreen
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
import com.example.realestatemanagementsystem.Home.Screens.HomeScreen
import com.example.realestatemanagementsystem.Home.Screens.SellScreen
import com.example.realestatemanagementsystem.user.UserProfile.AppDatabase
import com.example.realestatemanagementsystem.user.UserProfile.Screens.ProfileScreen
//import com.example.realestatemanagementsystem.user.UserProfile.Screens.UserProfileScreen
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import com.example.realestatemanagementsystem.user.authentication.Screens.SignInScreen
import com.example.realestatemanagementsystem.user.authentication.Screens.SignUpScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = (Screen.LoginScreen.route)
    ) {
//        composable(Screen.SignupScreen.route) {
//            SignUpScreen<Any>(
//                authViewModel,
//                navHostController=navController
//            )
//        }

        // Add the SignUpScreen route here
        composable(Screen.SignupScreen.route) {
            SignUpScreen(
                authViewModel = viewModel(),
                navController = navController,
                appDatabase = AppDatabase.getDatabase(LocalContext.current) // Pass the appDatabase here
            )
        }

        // Add the SignUpScreen route here

//        composable(Screen.LoginScreen.route) {
//            LoginScreen(
//                authViewModel,
//              navHostController = navController
//            )
//        }
        // Assuming you are using NavController to navigate

            composable(Screen.LoginScreen.route) {
                SignInScreen(
                    authViewModel = viewModel(),
                    navController = navController,
                    appDatabase = AppDatabase.getDatabase(LocalContext.current) // Pass the AppDatabase here
                )
            }

            composable("profile_screen/{email}") { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email")
                val context = LocalContext.current
                val appDatabase = AppDatabase.getDatabase(context) // Get the database instance
                if (email != null) {
                    ProfileScreen(email = email, userProfileDao = appDatabase.userProfileDao())
                }
            }

        composable(Screen.HomeScreen.route) {
            HomeScreen(
//                authViewModel,
                navHostController=navController
            )
        }
        composable(Screen.BuyScreen.route){
            BuyScreen(
                modifier=Modifier,
                navHostController = navController
            )
        }
        composable(Screen.SellScreen.route){
            SellScreen(
                modifier=Modifier,
                navHostController = navController
            )
        }
//        composable(Screen.UserProfileScreen.route){
//            UserProfileScreen(
//                UserProfileViewModel(application = Application())
//            )
//        }
    }
}