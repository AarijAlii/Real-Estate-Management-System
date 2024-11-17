package com.example.realestatemanagementsystem.Navigation

sealed class Screen(val route:String){

    data object LoginScreen : Screen("login_screen")
    data object SignupScreen: Screen("signup_screen")
    data object HomeScreen: Screen("home_screen")
    data object BuyScreen: Screen("buy_screen")
    data object SellScreen: Screen("sell_screen")
    data object UserProfileScreen :Screen("user_profile_screen/{email}")
    data object ProfileScreen : Screen("profile_screen/{email}")
    data object PropertyScreen : Screen("property_screen/{email}")
}