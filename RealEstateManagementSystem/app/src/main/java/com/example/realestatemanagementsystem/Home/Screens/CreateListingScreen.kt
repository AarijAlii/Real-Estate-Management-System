package com.example.realestatemanagementsystem.Home.Screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.realestatemanagementsystem.Property.Property
import com.example.realestatemanagementsystem.Property.PropertyViewModel
import kotlinx.coroutines.launch


@Composable
fun CreateListingScreen(email: String,
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
    var propertyName by remember { mutableStateOf("") }
    var isFormValid by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    val context = LocalContext.current
    val clientId = "68edc80df54e62f"

    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }

    // Image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> -> imageUris = uris }


    // Coroutine scope for launching suspend functions
    val coroutineScope = rememberCoroutineScope()
    // Check if form is valid


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
Column(modifier=Modifier.weight(2f).verticalScroll(scrollState).imePadding()) {
    Text(text = "Create Property Listing", style = MaterialTheme.typography.headlineLarge)

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
        onValueChange = { price = it },
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
        value = zipcode,
        onValueChange = { zipcode = it },
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
        if (imageUris.isNotEmpty()) {
            LazyRow {
                items(imageUris) { uri ->
                    val painter =rememberAsyncImagePainter(uri)
                    Image(
                        painter = painter,
                        contentDescription = "Selected image",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(100.dp), // Adjust size as needed
                        contentScale = ContentScale.Crop
                    )
                }
            }
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
            zipcode,
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
                        bathrooms = rooms.toInt(),
                        bedrooms = bedrooms.toInt(),
                        garage = garage.toInt(),
                        area = area.toDouble(),
                        type = type,
                        price = price.toDouble(),
                        zipCode = zipcode,
                        email = email
                    )

                    if (imageUris.isNotEmpty()) {
                        // Launch coroutine for suspend function
                        coroutineScope.launch {
                            propertyViewModel.addProperty(newProperty, imageUris, context, clientId)
                            navController.navigate("home_screen/$email"){}
                        }
                    } else {
                        errorMessage = "Please select at least one image."
                    }

                }
            }) {
            Text(text = "Create Listing")
        }
        Button(onClick = {imagePickerLauncher.launch("image/*")},modifier=Modifier.fillMaxWidth() , colors = ButtonColors(
            contentColor = Color.White,
            disabledContainerColor = Color.Gray,
            containerColor = Color.Red,
            disabledContentColor = Color.White,
        )){
            Text(text = "Add Photo")
        }
    }
}

