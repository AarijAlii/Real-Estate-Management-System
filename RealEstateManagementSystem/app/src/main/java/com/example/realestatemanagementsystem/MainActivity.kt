package com.example.realestatemanagementsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.realestatemanagementsystem.ui.theme.RealEstateManagementSystemTheme
import com.example.realestatemanagementsystem.user.authentication.AuthViewModel
import com.example.realestatemanagementsystem.user.authentication.HomeScreen
import com.example.realestatemanagementsystem.user.authentication.LoginScreen
import com.example.realestatemanagementsystem.user.authentication.Screen
import com.example.realestatemanagementsystem.user.authentication.SignUpScreen
import com.example.realestatemanagementsystem.user.profile.UserProfileScreen
import com.example.realestatemanagementsystem.user.profile.UserProfileViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: UserProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Assuming you're fetching user profile ID dynamically or hardcoding for demo
            //val userProfile = viewModel.userProfile.collectAsState()
            //val viewModel: UserProfileViewModel = viewModel()
            //val profile = viewModel.getProfile(1).collectAsState(initial = null)
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()
            RealEstateManagementSystemTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    //UserProfileScreen(viewModel)
                    NavigationGraph(navController, authViewModel)
                }
            }
        }
    }
}


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
                onNavigateToLogin = { navController.navigate(Screen.LoginScreen.route) },
                onNavigateToHome = { navController.navigate(Screen.HomeScreen.route) }
            )
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel,
                onNavigateToSignUp = { navController.navigate(Screen.SignupScreen.route) },
                onNavigateToHome = { navController.navigate(Screen.HomeScreen.route) }
            )
        }
        composable(Screen.HomeScreen.route) {
            HomeScreen(
                authViewModel,
                OnNavigateToLogin = { navController.navigate(Screen.LoginScreen.route) }
            )
        }
    }
}