package com.example.realestatemanagementsystem.util

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.realestatemanagementsystem.Property.Property
import com.example.realestatemanagementsystem.Property.PropertyDao
import com.example.realestatemanagementsystem.Property.PropertyViewModel
import com.example.realestatemanagementsystem.R
import com.example.realestatemanagementsystem.favorites.FavoriteViewModel
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyPropertyCards(
    modifier: Modifier = Modifier,
    email: String,
    propertyId: Int,
    property: Property,
    navHostController: NavHostController,
    viewModel: PropertyViewModel,
    favoriteViewModel: FavoriteViewModel,
    onBuy: () -> Unit,
    onclick: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val isLoading = remember { mutableStateOf(false) }
    val favoriteState = remember { mutableStateOf(false) }
    // Fetch the initial favorite state when the component is loaded
    LaunchedEffect(email, propertyId) {
        favoriteState.value = favoriteViewModel.isFavorite(email, propertyId)
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()  // Ensure the card fills the screen width
            .padding(8.dp)
            .clickable { onclick() }// Add padding around the card for spacing
            .shadow(16.dp, RoundedCornerShape(16.dp))  // Shadow and rounded corners
    ) {

        Column(modifier = Modifier) {
            // Image part: Ensure the image takes the full width of the card and is aligned to the start
            Image(
                painter = painterResource(R.drawable.house_file),
                contentDescription = "PropertyImage",
                modifier = Modifier
                    .size(400.dp, 200.dp)  // Make image take up the full width of the card
                    .clip(RoundedCornerShape(16.dp))
                    .graphicsLayer {
                        scaleX = 1.5f
                    }// Rounded corners for the image
                // Ensure no padding that could center the image
            )
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
                    property.price >= 10000000 -> {
                        val crore = (property.price / 10000000).toInt() // Get crore part and discard decimals
                        "$crore Crore" // Format price as Crore
                    }
                    property.price >= 100000 -> {
                        val lac = (property.price / 100000).toInt() // Get lac part and discard decimals
                        "$lac Lacs" // Format price as Lacs
                    }
                    else -> {
                        // For prices below 1 Lac, do not show the "Thousands" part.
                        property.price.toInt().toString() // Display the price as a whole number without decimals
                    }
                }
                Text(
                  text = text
                ,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.weight(1f)) // Pushes the other elements to the right
                Text(
                    text = buildAnnotatedString {
                        append("${property.area}yd") // Regular text
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
                Text(text = "${property.bedrooms} Bedrooms", fontSize = 12.sp, fontWeight = FontWeight.Light)
                Spacer(modifier = Modifier.width(8.dp))  // Space between text
                Text(text = "${property.rooms} Bathrooms", fontSize = 12.sp, fontWeight = FontWeight.Light)
            }
            // Address text
            Row(horizontalArrangement = Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${property.city}, ${property.state}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 8.dp)
                )}

            Row (verticalAlignment = Alignment.CenterVertically){


                Button(
                    onClick = {
                        viewModel.markAsSold(propertyId = property.propertyId.toInt())
                        onBuy()

                    },
                    colors = ButtonColors(
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        containerColor = Color.Red,
                        disabledContentColor = Color.White
                    ),
                    modifier = Modifier.weight(1f).padding(8.dp)
                ) {
                    Text(text = "Buy")

                }






                    // Favorite icon toggle logic

                    IconButton(onClick = {
                        if (isLoading.value) return@IconButton // Prevent double clicks while loading

                        // Optimistically toggle the favorite state
                        val previousState = favoriteState.value
                        favoriteState.value = !previousState
                        isLoading.value = true

                        coroutineScope.launch {
                            try {
                                // Perform database operation
                                favoriteViewModel.addOrRemoveFavorite(email, propertyId)
                            } catch (e: Exception) {
                                // Rollback state in case of failure
                                favoriteState.value = previousState
                                Log.e("FavoriteError", "Failed to toggle favorite: ${e.message}")
                            } finally {
                                isLoading.value = false
                            }
                        }

                    }) {
                        Icon(
                            imageVector = if (favoriteState.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite Icon",
                            tint =Color.Red
                        )
                    }
                }


        }

    }
}



