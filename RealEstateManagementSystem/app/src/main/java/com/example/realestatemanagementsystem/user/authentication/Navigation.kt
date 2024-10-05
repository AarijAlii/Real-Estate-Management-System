package com.example.realestatemanagementsystem.user.authentication

sealed class Screen(val route:String){
    data object LoginScreen: Screen("loginscreen")
    data object SignupScreen: Screen("signupscreen")
    data object HomeScreen: Screen("homescreen")
}