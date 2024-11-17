package com.example.realestatemanagementsystem.property

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember


@Composable
fun UpdatePropertyScreen(
    property: Property, // Pass the property to be updated
    propertyViewModel: PropertyViewModel,
    navController: NavHostController
) {
    var city by remember { mutableStateOf(property.city) }
    var state by remember { mutableStateOf(property.state) }
    var propertyNumber by remember { mutableStateOf(property.propertyNumber) }
    var rooms by remember { mutableStateOf(property.rooms.toString()) }
    var bedrooms by remember { mutableStateOf(property.bedrooms.toString()) }
    var garage by remember { mutableStateOf(property.garage.toString()) }
    var area by remember { mutableStateOf(property.area.toString()) }
    var type by remember { mutableStateOf(property.type) }
    var price by remember { mutableStateOf(property.price.toString()) }
    var zipcode by remember { mutableStateOf(property.zipCode) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Update Property", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        // Editable fields pre-filled with current property details
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

        // Save Button
        Button(
            onClick = {
                if (price.isNotEmpty() && rooms.isNotEmpty() && bedrooms.isNotEmpty()) {
                    val updatedProperty = Property(
                        propertyId = property.propertyId, // Use the existing ID
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
                        email = property.email // Keep the original user email
                    )
                    propertyViewModel.updateeProperty(updatedProperty)
                    navController.popBackStack() // Navigate back after saving
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }

        // Cancel Button
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}
