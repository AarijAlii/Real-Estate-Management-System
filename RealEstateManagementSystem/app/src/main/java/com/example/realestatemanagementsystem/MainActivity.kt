package com.example.realestatemanagementsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.realestatemanagementsystem.Home.Screens.CreateListingScreen
import com.example.realestatemanagementsystem.Home.Screens.HomeScreen
import com.example.realestatemanagementsystem.Navigation.NavigationGraph
import com.example.realestatemanagementsystem.Navigation.Screen
import com.example.realestatemanagementsystem.ui.theme.RealEstateManagementSystemTheme
import com.example.realestatemanagementsystem.user.UserProfile.Screens.UserProfileScreen
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import com.example.realestatemanagementsystem.user.authentication.Screens.SignUpScreen

class MainActivity : ComponentActivity() {
    private val userProfileViewModel: UserProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Assuming you're fetching user profile ID dynamically or hardcoding for demo
            //val userProfile = viewModel.userProfile.collectAsState()
            //val viewModel: UserProfileViewModel = viewModel()
            //val profile = viewModel.getProfile(1).collectAsState(initial = null)
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()
            RealEstateManagementSystemTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    //SignUpScreen(authViewModel,navController)
//                    HomeScreen(navController)
                   NavigationGraph(navController, authViewModel)
//                    CreateListingScreen(navHostController = navController, modifier = Modifier)
                }
            }
        }
    }
}


