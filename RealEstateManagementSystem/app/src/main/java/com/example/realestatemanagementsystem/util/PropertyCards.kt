package com.example.realestatemanagementsystem.util

import android.widget.ImageView
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.content.MediaType
import androidx.compose.foundation.content.MediaType.Companion.All
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realestatemanagementsystem.R

@Composable
fun PropertyCards(modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()  // Ensure the card fills the screen width
            .padding(8.dp)   // Add padding around the card for spacing
            .shadow(16.dp, RoundedCornerShape(16.dp))  // Shadow and rounded corners
    ) {
        Column(modifier = Modifier) {
            // Image part: Ensure the image takes the full width of the card and is aligned to the start
            Image(
                painter = painterResource(R.drawable.house_file),
                contentDescription = "PropertyImage",
                modifier = Modifier
                    .size(400.dp,200.dp)  // Make image take up the full width of the card
                    .clip(RoundedCornerShape(16.dp))
                    .graphicsLayer {
                        scaleX=1.5f
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
                Text(
                    text = "1.05 Crore",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.weight(1f)) // Pushes the other elements to the right
                Text(
                    text = buildAnnotatedString {
                        append("250yd") // Regular text
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
                Text(text = "3 Bedrooms", fontSize = 12.sp, fontWeight = FontWeight.Light)
                Spacer(modifier = Modifier.width(8.dp))  // Space between text
                Text(text = "3 Bathrooms", fontSize = 12.sp, fontWeight = FontWeight.Light)
            }
            // Address text
            Text(
                text = "FB Area Block 9, Karachi",
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 8.dp)
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
private fun PropertyCardPreview() {
    PropertyCards()
}