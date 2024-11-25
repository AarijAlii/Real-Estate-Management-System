package com.example.realestatemanagementsystem.util

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun DisplayImages(imageUrls: List<String>) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    // Example: Images will take up 1/4th of the screen width

    LazyRow {
        items(imageUrls) { imageurl ->
            AsyncImage(
                model = imageurl,
                contentDescription = "image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(250.dp)// Make image take up the full width of the card
                    .clip(RoundedCornerShape(16.dp))

            )
            Spacer(modifier = Modifier.size(8.dp))
        }
    }


}