package com.example.realestatemanagementsystem.property

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun AddPropertyScreen(
    email: String,
    navController: NavHostController,
    propertyViewModel: PropertyViewModel
) {
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var propertyNumber by remember { mutableStateOf("") }
    var rooms by remember { mutableStateOf("") }
    var bedrooms by remember { mutableStateOf("") }
    var garage by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var zipcode by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Add New Property", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        // Input fields for the property details
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state,
            onValueChange = { state = it },
            label = { Text("State") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = propertyNumber,
            onValueChange = { propertyNumber = it },
            label = { Text("Property Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = rooms,
            onValueChange = { rooms = it },
            label = { Text("Rooms") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = bedrooms,
            onValueChange = { bedrooms = it },
            label = { Text("Bedrooms") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = garage,
            onValueChange = { garage = it },
            label = { Text("Garage") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = area,
            onValueChange = { area = it },
            label = { Text("Area (sq. ft)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = type,
            onValueChange = { type = it },
            label = { Text("Type") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = zipcode,
            onValueChange = { zipcode = it },
            label = { Text("Zipcode") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Button to add the property
        Button(
            onClick = {
                if (price.isNotEmpty() && rooms.isNotEmpty() && bedrooms.isNotEmpty()) {
                    val newProperty = Property(
                        city = city,
                        state = state,
                        propertyNumber = propertyNumber,
                        rooms = rooms.toInt(),
                        bedrooms = bedrooms.toInt(),
                        garage = garage.toInt(),
                        area = area.toDouble(),
                        type = type,
                        price = price.toDouble(),
                        zipCode = zipcode,
                        email = email
                    )
                    propertyViewModel.adddProperty(newProperty)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Property")
        }
    }
}
