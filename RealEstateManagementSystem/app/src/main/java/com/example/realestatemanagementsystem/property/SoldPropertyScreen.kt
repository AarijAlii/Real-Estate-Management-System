package com.example.realestatemanagementsystem.property

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun SoldPropertiesScreen(
    email: String,
    propertyViewModel: PropertyViewModel,
    navController: NavHostController
) {
    val soldProperties by propertyViewModel.soldProperties.collectAsState()

    LaunchedEffect(email) {
        propertyViewModel.loadSoldListings(email)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Sold Properties", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        LazyColumn {
            items(soldProperties) { property ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("City: ${property.city}")
                        Text("Type: ${property.type}")
                        Text("Price: ${property.price}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}
