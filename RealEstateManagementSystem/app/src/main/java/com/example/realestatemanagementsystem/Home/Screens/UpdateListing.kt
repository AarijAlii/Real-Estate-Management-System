package com.example.realestatemanagementsystem.Home.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.realestatemanagementsystem.property.Property
import com.example.realestatemanagementsystem.property.PropertyDao
import com.example.realestatemanagementsystem.property.PropertyViewModel

@Composable
 fun UpdateListingScreen(propertyid: String,
                         navController: NavHostController,
                         propertyViewModel: PropertyViewModel,
                         propertyDao: PropertyDao

) {

    var zipcode by remember { mutableStateOf("") }
    var propertyName by remember { mutableStateOf("") }
    var isFormValid by remember { mutableStateOf(false) }
    var isSold by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current


    // Check if form is valid


    var property by remember { mutableStateOf<Property?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(propertyid) {
        try {
            // Fetch the user profile from the database in a coroutine
            val temp_property = propertyDao.getPropertyById(propertyId = propertyid.toInt())
            property = temp_property

        } catch (e: Exception) {
            errorMessage = "Failed to load profile: ${e.message}"

        }
    }

    if (property != null) {
        var zipCode by remember { mutableStateOf(property?.zipCode?:"") }
        var email by remember { mutableStateOf(property?.email?:"") }
        var city by remember { mutableStateOf(property?.city?:"") }
        var state by remember { mutableStateOf(property?.state?:"") }
        var propertyNumber by remember { mutableStateOf(property?.propertyNumber?:"") }
        var rooms by remember { mutableStateOf(property?.rooms.toString()?:"") }
        var bedrooms by remember { mutableStateOf(property?.bedrooms.toString()?:"") }
        var garage by remember { mutableStateOf(property?.garage.toString()?:"") }
        var area by remember { mutableStateOf(property?.area.toString()?:"") }
        var type by remember { mutableStateOf(property?.type.toString()?:"") }
        var price by remember { mutableStateOf(property?.price.toString()?:"") }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier=Modifier.weight(2f).verticalScroll(scrollState).imePadding()) {
                Text(text = "Update Property Listing", style = MaterialTheme.typography.headlineLarge)

                Spacer(modifier = Modifier.height(16.dp))

                // Property Name


                TextField(
                    value = propertyNumber,
                    onValueChange = { propertyNumber = it },
                    label = { Text("Property Number") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Type") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = price,
                    onValueChange = { price=it },
                    label = { Text("Price") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = rooms,
                    onValueChange = { rooms = it },
                    label = { Text("Rooms") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = bedrooms,
                    onValueChange = { bedrooms = it },
                    label = { Text("Bathrooms") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = garage,
                    onValueChange = { garage = it },
                    label = { Text("Garage") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = area,
                    onValueChange = { area = it },
                    label = { Text("Area") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = zipCode,
                    onValueChange = { zipCode = it },
                    label = { Text("Zipcode") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = state,
                    onValueChange = { state = it },
                    label = { Text("State") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus() // Dismiss the keyboard
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))



            // Submit button
            isFormValid = listOf(
                propertyNumber,
                rooms,
                bedrooms,
                garage,
                area,
                type,
                price,
                zipCode,
                city,
                state
            ).all { it.isNotBlank() }
            Button(modifier=Modifier.fillMaxWidth().padding(16.dp),
                colors =  ButtonColors(
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    containerColor = Color.Red,
                    disabledContentColor = Color.White,

                    ),
                enabled=isFormValid,
                onClick = {

                    if(isFormValid){
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
                            zipCode = zipCode,
                            email = email,
                            propertyId = propertyid.toInt(),
                            isSold = isSold

                        )
                        propertyViewModel.updateeProperty(newProperty)
                        navController.navigate("home_screen/$email"){
                            popUpTo("create_listing_screen/$email"){
                                inclusive=true
                            }}
                    }

                }) {
                Text(text = "Update Listing")
            }
        }
    }

    else {
        Text("User profile not found.")
    }

    if (errorMessage.isNotEmpty()) {
        Text(
            text = errorMessage,
            color = Color.Red,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

