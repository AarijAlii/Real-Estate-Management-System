package com.example.realestatemanagementsystem.property

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.realestatemanagementsystem.Property.PropertyViewModel
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileDao
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel

@Composable
fun BuyScreen(
    viewModel: PropertyViewModel,
    email: String,
    userProfileDao: UserProfileDao,
    navHostController: NavHostController,
    authViewModel: AuthViewModel,
    profileViewModel: UserProfileViewModel
) {
    val allProperties by viewModel.properties.collectAsState()
    val filteredProperties by viewModel.filteredProperties.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val searchText = remember { mutableStateOf("") }
    val cityFilter = remember { mutableStateOf("") }
    val stateFilter = remember { mutableStateOf("") }
    val minPrice = remember { mutableStateOf("") }
    val maxPrice = remember { mutableStateOf("") }
    val sortOrder = remember { mutableStateOf("Ascending") }

    // Fetch all properties initially
    LaunchedEffect(Unit) {
        viewModel.getAllProperties()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Buy Properties", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        // Search bar
        TextField(
            value = searchText.value,
            onValueChange = { searchText.value = it },
            label = { Text("Search Property ID or User Email") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            // City filter
            TextField(
                value = cityFilter.value,
                onValueChange = { cityFilter.value = it },
                label = { Text("City") },
                modifier = Modifier.weight(1f).padding(end = 4.dp)
            )

            // State filter
            TextField(
                value = stateFilter.value,
                onValueChange = { stateFilter.value = it },
                label = { Text("State") },
                modifier = Modifier.weight(1f).padding(start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            // Min price filter
            TextField(
                value = minPrice.value,
                onValueChange = { minPrice.value = it },
                label = { Text("Min Price") },
                modifier = Modifier.weight(1f).padding(end = 4.dp)
            )

            // Max price filter
            TextField(
                value = maxPrice.value,
                onValueChange = { maxPrice.value = it },
                label = { Text("Max Price") },
                modifier = Modifier.weight(1f).padding(start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Sort order selection
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Sort Order:")
            RadioButton(
                selected = sortOrder.value == "Ascending",
                onClick = { sortOrder.value = "Ascending" }
            )
            Text(text = "Ascending")
            RadioButton(
                selected = sortOrder.value == "Descending",
                onClick = { sortOrder.value = "Descending" }
            )
            Text(text = "Descending")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Search button
        Button(onClick = {
            if (searchText.value.isNotEmpty()) {
                if (searchText.value.toIntOrNull() != null) {
                    viewModel.searchByPropertyId(searchText.value.toInt())
                } else {
                    viewModel.searchByEmail(searchText.value)
                }
            } else {
                viewModel.filterProperties(
                    city = cityFilter.value.takeIf { it.isNotEmpty() },
                    state = stateFilter.value.takeIf { it.isNotEmpty() },
                    minPrice = minPrice.value.toDoubleOrNull(),
                    maxPrice = maxPrice.value.toDoubleOrNull(),
                    zipCode = null,
                    type = null,
                    noOfRooms = null,
                    bedrooms = null,
                    garage = null,
                    sortOrder = sortOrder.value
                )
            }
        }) {
            Text(text = "Search / Filter")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show error message if any
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(8.dp))
        }

        // Display properties or message if no properties are found
        LazyColumn {
            val propertiesToDisplay = when {
                searchResults.isNotEmpty() -> searchResults
                filteredProperties.isNotEmpty() -> filteredProperties
                else -> emptyList() // Ensure empty list if no filters match
            }

            if (propertiesToDisplay.isEmpty()) {
                item {
                    Text(text = "No properties found matching the filters.", modifier = Modifier.padding(16.dp))
                }
            } else {
                items(propertiesToDisplay) { property ->
                    PropertyCard(property)
                }
            }
        }
    }
}

@Composable
fun PropertyCard(property: Property) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Property ID: ${property.propertyId}")
            Text(text = "City: ${property.city}")
            Text(text = "State: ${property.state}")
            Text(text = "Price: $${property.price}")
            Text(text = "Type: ${property.type}")
        }
    }
}

//@Composable
//fun BuyScreen(
//    viewModel: PropertyViewModel,
//    email: String,
//    userProfileDao: UserProfileDao,
//    navHostController: NavHostController,
//    authViewModel: AuthViewModel,
//    profileViewModel: UserProfileViewModel
//) {
//    val allProperties by viewModel.properties.collectAsState()
//    val filteredProperties by viewModel.filteredProperties.collectAsState()
//    val searchResults by viewModel.searchResults.collectAsState()
//    val errorMessage by viewModel.errorMessage.collectAsState()
//
//    val searchText = remember { mutableStateOf("") }
//    val cityFilter = remember { mutableStateOf("") }
//    val stateFilter = remember { mutableStateOf("") }
//    val minPrice = remember { mutableStateOf("") }
//    val maxPrice = remember { mutableStateOf("") }
//    val sortOrder = remember { mutableStateOf("Ascending") }
//
//    // Fetch all properties initially
//    LaunchedEffect(Unit) {
//        viewModel.getAllProperties()
//    }
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        Text(text = "Buy Properties", style = MaterialTheme.typography.titleLarge)
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Search bar
//        TextField(
//            value = searchText.value,
//            onValueChange = { searchText.value = it },
//            label = { Text("Search Property ID or User Email") },
//            modifier = Modifier.fillMaxWidth(),
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Row(modifier = Modifier.fillMaxWidth()) {
//            // City filter
//            TextField(
//                value = cityFilter.value,
//                onValueChange = { cityFilter.value = it },
//                label = { Text("City") },
//                modifier = Modifier.weight(1f).padding(end = 4.dp)
//            )
//
//            // State filter
//            TextField(
//                value = stateFilter.value,
//                onValueChange = { stateFilter.value = it },
//                label = { Text("State") },
//                modifier = Modifier.weight(1f).padding(start = 4.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Row(modifier = Modifier.fillMaxWidth()) {
//            // Min price filter
//            TextField(
//                value = minPrice.value,
//                onValueChange = { minPrice.value = it },
//                label = { Text("Min Price") },
//                modifier = Modifier.weight(1f).padding(end = 4.dp)
//            )
//
//            // Max price filter
//            TextField(
//                value = maxPrice.value,
//                onValueChange = { maxPrice.value = it },
//                label = { Text("Max Price") },
//                modifier = Modifier.weight(1f).padding(start = 4.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Sort order selection
//        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//            Text(text = "Sort Order:")
//            RadioButton(
//                selected = sortOrder.value == "Ascending",
//                onClick = { sortOrder.value = "Ascending" }
//            )
//            Text(text = "Ascending")
//            RadioButton(
//                selected = sortOrder.value == "Descending",
//                onClick = { sortOrder.value = "Descending" }
//            )
//            Text(text = "Descending")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Search button
//        Button(onClick = {
//            if (searchText.value.isNotEmpty()) {
//                if (searchText.value.toIntOrNull() != null) {
//                    viewModel.searchByPropertyId(searchText.value.toInt())
//                } else {
//                    viewModel.searchByEmail(searchText.value)
//                }
//            } else {
//                viewModel.filterProperties(
//                    city = cityFilter.value.takeIf { it.isNotEmpty() },
//                    state = stateFilter.value.takeIf { it.isNotEmpty() },
//                    minPrice = minPrice.value.toDoubleOrNull(),
//                    maxPrice = maxPrice.value.toDoubleOrNull(),
//                    zipCode = null,
//                    type = null,
//                    noOfRooms = null,
//                    bedrooms = null,
//                    garage = null,
//                    sortOrder = sortOrder.value
//                )
//            }
//        }) {
//            Text(text = "Search / Filter")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Show error message if any
//        if (errorMessage.isNotEmpty()) {
//            Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(8.dp))
//        }
//
//        // Display properties or message if no properties are found
//        LazyColumn {
//            val propertiesToDisplay = when {
//                searchResults.isNotEmpty() -> searchResults
//                filteredProperties.isNotEmpty() -> filteredProperties
//                else -> allProperties // Initially show all properties when no filters are applied
//            }
//
//            if (propertiesToDisplay.isEmpty()) {
//                item {
//                    Text(text = "No properties found matching the filters.", modifier = Modifier.padding(16.dp))
//                }
//            } else {
//                items(propertiesToDisplay) { property ->
//                    PropertyCard(property)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun PropertyCard(property: Property) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = "Property ID: ${property.propertyId}")
//            Text(text = "City: ${property.city}")
//            Text(text = "State: ${property.state}")
//            Text(text = "Price: $${property.price}")
//            Text(text = "Type: ${property.type}")
//        }
//    }
//}
