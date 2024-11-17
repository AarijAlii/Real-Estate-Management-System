package com.example.realestatemanagementsystem.Home.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.realestatemanagementsystem.util.PropertyCards

@Composable
fun BuyScreen(modifier: Modifier = Modifier,navHostController: NavHostController,innerPadding:PaddingValues) {
    Column (modifier = Modifier
    .padding(innerPadding)
    .fillMaxWidth()
    .padding(16.dp)){
    PropertyCards(modifier = Modifier)

}
}