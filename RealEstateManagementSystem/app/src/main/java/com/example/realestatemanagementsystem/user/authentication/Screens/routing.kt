package com.example.realestatemanagementsystem.user.authentication.Screens

sealed class Screeen(val route: String) {
    object SignnUpScreen : Screeen("signup")
    object SignInnScreen : Screeen("signin")
    object SuccessScreen : Screeen("success")
}
