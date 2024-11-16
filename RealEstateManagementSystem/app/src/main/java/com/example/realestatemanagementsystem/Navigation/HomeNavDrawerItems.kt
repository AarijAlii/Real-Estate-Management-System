package com.example.realestatemanagementsystem.Navigation

import com.example.realestatemanagementsystem.R

data class NavigationItems(val icon: Int, val title:String,val route:String)
fun getNavigationItems():List<NavigationItems>{
    val items= listOf<NavigationItems>(
        NavigationItems(R.drawable.buy,"Buy",Screen.BuyScreen.route),
        NavigationItems(R.drawable.sell,"Sell",Screen.SellScreen.route)


)
    return items
}