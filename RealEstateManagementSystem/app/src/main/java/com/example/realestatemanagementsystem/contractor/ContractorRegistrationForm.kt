package com.example.realestatemanagementsystem.contractor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ContractorFormScreen(
    email: String,
    contractorViewModel: ContractorViewModel,
    onRegistrationComplete: () -> Unit
) {
    var experience by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var speciality by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") } // To store error message

    // Check if contractor already exists by email
    LaunchedEffect(email) {
        val contractor = contractorViewModel.getContractorByEmail(email)
        if (contractor != null) {
            errorMessage = "You are already signed up as a contractor"
        } else {
            errorMessage = ""
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Contractor Registration", style = MaterialTheme.typography.bodyMedium)
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
            value = contact,
            onValueChange = { contact = it },
            label = { Text("Contact Number") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = speciality,
            onValueChange = { speciality = it },
            label = { Text("Speciality (e.g., Plumbing)") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (errorMessage.isEmpty()) {
                    contractorViewModel.insertContractor(
                        email = email,
                        experience = experience,
                        contact = contact,
                        speciality = speciality,
                        overallRating = 0.0f,
                    )
                    onRegistrationComplete()
                }
            },
        ) {
            Text(text = "Register")
        }
    }
}
