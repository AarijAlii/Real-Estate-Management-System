package com.example.realestatemanagementsystem.Home.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.realestatemanagementsystem.Property.Property
import com.example.realestatemanagementsystem.Property.PropertyViewModel
import com.example.realestatemanagementsystem.util.formatPrice


@Composable
fun PropertyComparisonTable( viewModel: PropertyViewModel, innerPadding: PaddingValues) {
    // Collect the compareList state
    val compareList by viewModel.compareList.collectAsState()

    // Only show if compareList is not empty


    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(Modifier.padding(16.dp))

        // Header Row
        Row(horizontalArrangement = Arrangement.Center) {
            Spacer(modifier = Modifier.weight(1f))
            Text("Property 1", modifier = Modifier.weight(1f))
            Text("Property 2", modifier = Modifier.weight(1f))
            Text("Property 3")
        }

        Spacer(Modifier.padding(16.dp))
        Divider(modifier = Modifier.wrapContentWidth())

        // Displaying comparison values
        ComparisonRow("Price", compareList) { property -> property?.price?.let { formatPrice(it) } ?: "-" }
        Spacer(Modifier.padding(16.dp))
        ComparisonRow("City", compareList) { property -> property?.city ?: "-" }
        Spacer(Modifier.padding(16.dp))
        ComparisonRow("State", compareList) { property -> property?.state ?: "-" }
        Spacer(Modifier.padding(16.dp))
        ComparisonRow("Bedrooms", compareList) { property -> property?.bedrooms?.toString() ?: "-" }
        Spacer(Modifier.padding(16.dp))
        ComparisonRow("Bathrooms", compareList) { property -> property?.bathrooms?.toString() ?: "-" }
        Spacer(Modifier.padding(16.dp))
        ComparisonRow("Area", compareList) { property -> property?.area?.toString() ?: "-" }
        Spacer(Modifier.padding(16.dp))
        ComparisonRow("Garage", compareList) { property -> property?.garage?.toString() ?: "-" }
        Spacer(Modifier.padding(16.dp))

        Divider(modifier = Modifier.wrapContentWidth())

        // Remove buttons for each property
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.weight(1f))
            compareList.forEachIndexed { index, _ ->
                if (index < 3) {
                    Button(
                        onClick = { viewModel.removeCompareList(index) },
                        modifier = Modifier.padding(start =8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Remove")
                    }
                }
            }
        }
    }
}

@Composable
fun ComparisonRow(attribute: String, compareList: List<Property?>, valueProvider: (Property?) -> String) {
    Row(horizontalArrangement = Arrangement.Start) {
        Text(attribute, modifier = Modifier.weight(1f))

        // Safely access each property, providing fallback "-"
        for (i in 0 until 3) {
            Text(
                text = valueProvider(compareList.getOrNull(i)),
                modifier = Modifier.weight(1f)
            )
        }
    }
    Divider(modifier = Modifier.wrapContentWidth())
}



