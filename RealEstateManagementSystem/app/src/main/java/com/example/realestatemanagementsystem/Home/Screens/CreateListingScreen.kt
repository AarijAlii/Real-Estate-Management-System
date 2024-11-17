package com.example.realestatemanagementsystem.Home.Screens

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.realestatemanagementsystem.Navigation.Screen

@Composable
fun CreateListingScreen(modifier: Modifier = Modifier,navHostController: NavHostController) {

    var propertyName by remember { mutableStateOf("") }
    var propertyDescription by remember { mutableStateOf("") }
    var propertyPrice by remember { mutableStateOf("") }
    var propertyLocation by remember { mutableStateOf("") }
    var propertyImageUri by remember { mutableStateOf<Uri?>(null) }
    var isFormValid by remember { mutableStateOf(false) }

    // Check if form is valid
    LaunchedEffect(propertyName, propertyDescription, propertyPrice, propertyLocation) {
        isFormValid = propertyName.isNotBlank() && propertyDescription.isNotBlank() &&
                propertyPrice.isNotBlank() && propertyLocation.isNotBlank()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Create Property Listing", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Property Name
        OutlinedTextField(
            value = propertyName,
            onValueChange = { propertyName = it },
            label = { Text("Property Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Property Description
        OutlinedTextField(
            value = propertyDescription,
            onValueChange = { propertyDescription = it },
            label = { Text("Property Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Property Price
        OutlinedTextField(
            value = propertyPrice,
            onValueChange = { propertyPrice = it },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions.Default,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Property Location
        OutlinedTextField(
            value = propertyLocation,
            onValueChange = { propertyLocation = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Image picker (basic)
        Button(onClick = {
            // Add functionality to pick image from gallery (e.g., using ImagePicker or other methods)
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Pick Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit button
        Button(
            onClick = {
                // Handle form submission here
                if (isFormValid) {
                    // You can send the form data to a ViewModel or directly handle it here
                    // For now, we just show a log message
                    println("Property Listing Created: $propertyName, $propertyDescription, $propertyPrice, $propertyLocation")
                    // Navigate to another screen or show a success message
                    navHostController.navigate("next_screen")  // Example navigation
                }
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Submit Listing")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateListingScreenPreview() {
    CreateListingScreen(navHostController = rememberNavController())
}