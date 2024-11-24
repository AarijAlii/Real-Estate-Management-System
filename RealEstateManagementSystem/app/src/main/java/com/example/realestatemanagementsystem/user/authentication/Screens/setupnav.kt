package com.example.realestatemanagementsystem.user.authentication.Screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.realestatemanagementsystem.user.authentication.AuthenticationViewModel

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screeen.SignInnScreen.route) {
        composable(Screeen.SignnUpScreen.route) {
            val authViewModel: AuthenticationViewModel = viewModel()
            SignnUpScreen(authViewModel = authViewModel, navController = navController)
        }
        composable(Screeen.SignInnScreen.route) {
            val authViewModel: AuthenticationViewModel = viewModel()
            SignInnScreen(authViewModel = authViewModel, navController = navController)
        }
        composable(Screeen.SuccessScreen.route) {
            SuccessScreen()
        }
    }
}
