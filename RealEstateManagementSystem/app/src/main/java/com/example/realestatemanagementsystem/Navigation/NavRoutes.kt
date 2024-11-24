package com.example.realestatemanagementsystem.Navigation

sealed class Screen(val route:String){
    data object LoginScreen: Screen("login_screen")
    data object SignupScreen: Screen("signup_screen")
    data object HomeScreen: Screen("home_screen/{email}")
    data object ContractorRegistrationForm:Screen("contractor_screen_registration/{email}")
    data object UserProfileScreen :Screen("profile_screen/{email}")
    data object CreateListingScreen :Screen("create_listing_screen/{email}")
    data object UserProfileUpdateScreen:Screen("update_profile_screen/{email}")
    data object UpdateListingScreen:Screen("update_listing_screen/{email}")
    data object ScheduleAppointmentScreen:Screen("schedule_appointment_screen/{propertyId}/{ownerEmail}")
    data object AppointmentScreen:Screen("appointment_screen")

}