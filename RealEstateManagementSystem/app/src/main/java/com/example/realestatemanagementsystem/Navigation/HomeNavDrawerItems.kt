package com.example.realestatemanagementsystem.Navigation

import com.example.realestatemanagementsystem.R

data class NavigationItems(val icon: Int, val title:String)
fun getNavigationItems():List<NavigationItems>{
    val items= listOf(
        NavigationItems(R.drawable.buy,"Buy"),
        NavigationItems(R.drawable.sell,"Sell"),
//        NavigationItems(R.drawable.baseline_logout_24,"Logout",Screen.LoginScreen.route)


)
    return items
}