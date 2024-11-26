package com.example.realestatemanagementsystem.Home.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.realestatemanagementsystem.contractor.ContractorViewModel

@Composable
fun ContractorFormScreen(
    email: String,
    contractorViewModel: ContractorViewModel,
    onRegistrationComplete: () -> Unit
) {

    var experience by remember { mutableStateOf("") }
    var Rates by remember { mutableStateOf("") }
    var speciality by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") } // To store error message

    // Check if contractor already exists by email


    Box(contentAlignment = Alignment.Center,modifier = Modifier.wrapContentHeight()) {
        Column(
            modifier = Modifier

                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Contractor Registration", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage)
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedTextField(
                value = experience,
                onValueChange = { experience = it },
                label = { Text("Experience (e.g., 5 years)") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = Rates,
                onValueChange = { Rates = it },
                label = { Text("Rates in PKR") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = speciality,
                onValueChange = { speciality = it },
                label = { Text("Speciality (e.g., Plumbing)") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row (horizontalArrangement = Arrangement.End){
                Button(
                    onClick = {
                        if (errorMessage.isEmpty()) {
                            contractorViewModel.insertContractor(
                                email = email,
                                experience = experience,
                                Rate = Rates,
                                speciality = speciality,
                                overallRating = 0.0f,
                            )
                            onRegistrationComplete()
                        }
                    },
                    colors = ButtonColors(
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        containerColor = Color.Red,
                        disabledContentColor = Color.White,
                    )
                ) {
                    Text(text = "Register")
                }
            }
        }
    }
}