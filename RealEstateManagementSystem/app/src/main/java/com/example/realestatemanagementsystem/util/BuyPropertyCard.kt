package com.example.realestatemanagementsystem.util

import android.app.Application
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.realestatemanagementsystem.Property.Property
import com.example.realestatemanagementsystem.Property.PropertyViewModel
import com.example.realestatemanagementsystem.favorites.FavoriteViewModel
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

    LaunchedEffect(email, propertyId) {
        favoriteState.value = favoriteViewModel.isFavorite(email, propertyId)
    }
    val context = LocalContext.current
    val application = context.applicationContext as Application

    // Fetch images for the property
    val imageUrls by viewModel.imageUrls.collectAsState()
    val customImageLoader = createCustomImageLoader(application)
    viewModel.fetchImagesForProperty(propertyId)



    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(400.dp)
            .clickable { onclick() }
            .shadow(8.dp, RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                ,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Display Images
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center // Adjust height for the image area
            ) {
                if (imageUrls.isNotEmpty()) {
                    DisplayImages(imageUrls, customImageLoader)
                }
                else{
                    Text(text = "No images available")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Price and Area Information
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PKR ${formatPrice(property.price)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${property.area} yd\u00B2", // Use Unicode for superscript ²
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Bedroom and Bathroom Information
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${property.bedrooms} Bedrooms",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
                Text(
                    text = "${property.rooms} Bathrooms",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // City and State
            Text(
                text = "${property.city}, ${property.state}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Buttons: Buy and Favorite
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        viewModel.markAsSold(propertyId = propertyId)
                        onBuy()
                    },
                    colors = ButtonColors(
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        containerColor = Color.Red,
                        disabledContentColor = Color.White,
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Buy")
                }

                IconButton(
                    onClick = {
                        if (isLoading.value) return@IconButton
                        val previousState = favoriteState.value
                        favoriteState.value = !previousState
                        isLoading.value = true

                        coroutineScope.launch {
                            try {
                                favoriteViewModel.addOrRemoveFavorite(email, propertyId)
                            } catch (e: Exception) {
                                favoriteState.value = previousState
                                Log.e("FavoriteError", "Error toggling favorite: ${e.message}")
                            } finally {
                                isLoading.value = false
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (favoriteState.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun DisplayImages(imageUrls: List<String>,customImageLoader:coil.ImageLoader) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(imageUrls) { imageUrl ->
            Image(
                painter = rememberAsyncImagePainter(imageUrl,customImageLoader),
                contentDescription = "Property Image",
                modifier = Modifier
                    .size(150.dp) // Resize image to a more reasonable size
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

fun formatPrice(price: Double): String {
    return when {
        price >= 1_00_00_000 -> "${(price / 1_00_00_000).toInt()} Crore"
        price >= 1_00_000 -> "${(price / 1_00_000).toInt()} Lacs"
        else -> price.toInt().toString()
    }
}
