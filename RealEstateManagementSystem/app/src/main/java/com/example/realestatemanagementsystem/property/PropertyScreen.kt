package com.example.realestatemanagementsystem.property

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun PropertyScreen(
    email: String,
    propertyViewModel: PropertyViewModel,
    navController: NavHostController
) {
    // Collect properties and error message state from ViewModel
    val unsoldProperties by propertyViewModel.unsoldProperties.collectAsState(initial = emptyList())
    val errorMessage by propertyViewModel.errorMessage.collectAsState()

    // Load the unsold properties when the screen is displayed
    LaunchedEffect(email) {
        propertyViewModel.loadCurrentListings(email)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Your Properties", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        // Display error message if any
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
        }

        // List of unsold properties
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(unsoldProperties) { property ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("City: ${property.city}")
                        Text("Type: ${property.type}")
                        Text("Price: ${property.price}")

                        // Update and Delete buttons
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(onClick = {
                                navController.navigate("update_property_screen/${property.propertyId}")
                               // navController.navigate("update_property_screen?property=${Gson().toJson(property)}")

                            }) {
                                Text("Update")
                            }
                            Button(onClick = {
                                propertyViewModel.deleteeProperty(property.propertyId)
                            }) {
                                Text("Delete")
                            }
                        }

                        Spacer(modifier = Modifier.padding(4.dp))

                        // Mark as Sold button
                        Button(onClick = {
                            propertyViewModel.markAsSold(property.propertyId)
                        }) {
                            Text("Mark as Sold")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        // Button to navigate to Add New Property screen
        Button(
            onClick = { navController.navigate("add_property_screen/$email") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add New Property")
        }

        Spacer(modifier = Modifier.padding(8.dp))

        // Button to navigate to Sold Properties screen
        Button(
            onClick = { navController.navigate("sold_properties_screen/$email") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Sold Properties")
        }
    }
}
