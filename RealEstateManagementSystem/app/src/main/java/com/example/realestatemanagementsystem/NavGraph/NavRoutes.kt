package com.example.realestatemanagementsystem.NavGraph

sealed class Screen(val route:String){
    data object LoginScreen: Screen("loginscreen")
    data object SignupScreen: Screen("signupscreen")
    data object HomeScreen: Screen("homescreen")
}