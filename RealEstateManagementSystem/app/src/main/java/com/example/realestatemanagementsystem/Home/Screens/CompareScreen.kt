package com.example.realestatemanagementsystem.Home.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realestatemanagementsystem.Property.Property
import com.example.realestatemanagementsystem.Property.PropertyViewModel


@Composable
fun PropertyComparisonTable(properties: List<Property>, viewModel: PropertyViewModel) {
    val compareList by viewModel.compareList.collectAsState()

    // Only show if there are 1 to 3 properties
    if (compareList.isEmpty()) {
        Text("No properties to compare", modifier = Modifier.padding(16.dp))
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(8.dp)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            // Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray)
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Attributes", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                compareList.forEachIndexed { index, property ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.LightGray)
                            .padding(8.dp)
                            .border(BorderStroke(1.dp, Color.Black))
                    ) {
                        Text(text = "Property ${property?.propertyId}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = { viewModel.removeCompareList(index) },
                            colors = ButtonDefaults.buttonColors(Color.Red),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Remove", color = Color.White)
                        }
                    }
                }
            }
        }

        // Comparison rows for each attribute
        item { ComparisonRow("City", compareList) { it.city } }
        item { ComparisonRow("State", compareList) { it.state } }
        item { ComparisonRow("Property No.", compareList) { it.propertyNumber } }
        item { ComparisonRow("Rooms", compareList) { it.rooms.toString() } }
        item { ComparisonRow("Bedrooms", compareList) { it.bedrooms.toString() } }
        item { ComparisonRow("Garage", compareList) { it.garage.toString() } }
        item { ComparisonRow("Area (sq ft)", compareList) { it.area.toString() } }
        item { ComparisonRow("Type", compareList) { it.type } }
        item { ComparisonRow("Price (PKR)", compareList) { it.price.toString() } }
        item { ComparisonRow("Zip Code", compareList) { it.zipCode } }
        item { ComparisonRow("Sold Status", compareList) { if (it.isSold) "Sold" else "Available" } }
    }
}

@Composable
fun ComparisonRow(
    attributeName: String,
    properties: MutableList<Property?>,
    valueProvider: (Property) -> String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 4.dp)
            .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(text = attributeName, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
        properties.forEach { property ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            ) {
                Text(text = "${property?.let { valueProvider(it) }}", fontSize = 12.sp)
            }
        }
    }
}

