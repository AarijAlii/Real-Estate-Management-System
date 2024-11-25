package com.example.realestatemanagementsystem.util

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.realestatemanagementsystem.AppDatabase
import com.example.realestatemanagementsystem.Property.PropertyViewModel
import com.example.realestatemanagementsystem.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellPropertyCards(modifier: Modifier = Modifier, area:String, city:String, state:String, bedrooms:String, bathrooms:String, price:Double, propertyId:String, navHostController:NavHostController,propertyViewModel: PropertyViewModel, onDeleted:()->Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val imageUrls = remember { mutableStateOf<List<String>>(emptyList()) }
    val imageDao = AppDatabase.getDatabase(LocalContext.current).imageDao()
    LaunchedEffect(propertyId){
        imageUrls.value=imageDao.getImageUrlsForProperty(propertyId.toInt())

    }
    Card(
        modifier = Modifier
            .fillMaxWidth()  // Ensure the card fills the screen width
            .padding(8.dp)   // Add padding around the card for spacing
            .shadow(16.dp, RoundedCornerShape(16.dp))  // Shadow and rounded corners
    ) {
        Column(modifier = Modifier) {
            // Image part: Ensure the image takes the full width of the card and is aligned to the start
            if (imageUrls.value.isNotEmpty()) {
                // Use rememberAsyncImagePainter to cache the image

                DisplayImages(imageUrls.value)
            }
            else{
                Box(modifier=Modifier.height(200.dp).fillMaxWidth(), contentAlignment = Alignment.Center){
                    Text("No Image Available")
                }
            }
            // Price and area row
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            ) {
                Text(
                    text = "PKR ",
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                val text = when {
                    price >= 10000000 -> {
                        val crore = (price / 10000000).toInt() // Get crore part and discard decimals
                        "$crore Crore" // Format price as Crore
                    }
                    price >= 100000 -> {
                        val lac = (price / 100000).toInt() // Get lac part and discard decimals
                        "$lac Lacs" // Format price as Lacs
                    }
                    else -> {
                        // For prices below 1 Lac, do not show the "Thousands" part.
                        price.toInt().toString() // Display the price as a whole number without decimals
                    }
                }

                Text(
                    text = text,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.weight(1f)) // Pushes the other elements to the right
                Text(
                    text = buildAnnotatedString {
                        append("${area}yd") // Regular text
                        withStyle(style = SpanStyle(
                            baselineShift = BaselineShift.Superscript, // Make "2" superscript
                            fontSize = 12.sp // Optional, you can change the font size of the superscript
                        )) {
                            append("2")
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }
            // Bedroom and bathroom row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 4.dp)
            ) {
                Text(text = "$bedrooms Bedrooms", fontSize = 12.sp, fontWeight = FontWeight.Light)
                Spacer(modifier = Modifier.width(8.dp))  // Space between text
                Text(text = "$bathrooms Bathrooms", fontSize = 12.sp, fontWeight = FontWeight.Light)
            }
            // Address text
            Row(horizontalArrangement = Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$city, $state",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 8.dp)
                )
                Spacer(modifier=Modifier.weight(1f))

                IconButton(modifier = Modifier
                    .padding(8.dp)
                    .size(22.dp), onClick = {

                    navHostController.navigate("update_listing_screen/$propertyId")}
                )
                    {
                    Icon(painter = painterResource(R.drawable.baseline_edit_24), contentDescription = "edit")
                }
                IconButton(modifier = Modifier
                    .padding(8.dp)
                    .size(22.dp), onClick = {
                    showDeleteDialog = true

                }) {
                    Icon(painter = painterResource(R.drawable.baseline_delete_24), contentDescription = "delete",tint= Color.Red)
                }
            }
        }
        if (showDeleteDialog) {
            AlertDialog(onDismissRequest = {showDeleteDialog=false},
                title = { Text(text = "Delete Property") },
                text = { Text(text = "Are you sure you want to delete this property?") },
                confirmButton = {
                    Button(onClick = {

                        coroutineScope.launch {
                        deleteProperty(propertyId = propertyId, propertyViewModel = propertyViewModel)
                        onDeleted()
                        }
                        showDeleteDialog = false
                    }) {
                        Text(text = "Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteDialog = false }) {
                        Text(text = "No")
                    }
                }
            )
        }
    }
}


suspend fun deleteProperty(modifier: Modifier = Modifier, propertyId: String, propertyViewModel: PropertyViewModel) {

        try{
            propertyViewModel.deleteeProperty(propertyId = propertyId.toInt())
        }catch (e:Exception){
            val errorMessage = e.message.toString()
            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

}




