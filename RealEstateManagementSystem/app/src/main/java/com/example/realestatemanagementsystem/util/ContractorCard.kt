package com.example.realestatemanagementsystem.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.realestatemanagementsystem.R
import com.example.realestatemanagementsystem.contractor.ContractorWithUserProfile

@Composable
fun ContractorCard(modifier: Modifier = Modifier,innerPadding:PaddingValues,contractor: ContractorWithUserProfile) {
    Card(
        modifier = Modifier

            .padding()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
        ) {
            // Image Section
            Image(
                painter = painterResource(id = R.drawable.house_file), // Replace with your image resource
                contentDescription = "Card Image",
                contentScale = ContentScale.Crop,

                modifier = Modifier
                    .weight(1f)

                    .padding(2.dp)
                    .shadow(16.dp, RoundedCornerShape(16.dp))
            )

            // Separator

            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .padding(top = 16.dp, start = 8.dp)
                    .width(1.dp)
                    .height(100.dp)


            )

            // Information Section
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${contractor.firstName} ${contractor.lastName}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = contractor.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = contractor.contact,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )

                }

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "Deals in: ${contractor.speciality}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { /* Handle button click */ },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Color.Red,
                            disabledContentColor = Color.Gray
                        ),
                    ) {
                        Text(text = "Hire")
                    }
                }
            }
        }
    }
}

