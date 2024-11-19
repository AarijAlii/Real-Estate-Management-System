package com.example.realestatemanagementsystem.Navigation

sealed class Screen(val route:String){
    data object LoginScreen: Screen("login_screen")
    data object SignupScreen: Screen("signup_screen")
    data object HomeScreen: Screen("home_screen/{email}")
    data object BuyScreen: Screen("buy_screen/{email}")
    data object SellScreen : Screen("sell_screen/{email}")
    data object UserProfileScreen :Screen("profile_screen/{email}")
    data object CreateListingScreen :Screen("create_listing_screen/{email}")

}