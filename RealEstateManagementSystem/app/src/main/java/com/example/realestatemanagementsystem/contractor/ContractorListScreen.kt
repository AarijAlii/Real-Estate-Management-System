package com.example.realestatemanagementsystem.contractor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractorListScreen(
    contractorViewModel: ContractorViewModel,
    navController: NavHostController,
    ) {
    // Trigger the fetch when the screen loads
    LaunchedEffect(Unit) {
        contractorViewModel.fetchAllContractorsWithDetails()
    }

    // Observe the contractors LiveData
    val contractors by contractorViewModel.contractors.observeAsState(emptyList())

    // Display the list using LazyColumn
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contractors List") }
            )
        }
    ) { paddingValues ->
        if (contractors.isEmpty()) {
            // Show message if no contractors
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No contractors available")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(contractors) { contractor ->
                    ContractorItem(contractor)
                }
            }
        }
    }
}


@Composable
fun ContractorItem(contractor: ContractorWithUserProfile) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // You can replace this with an Image for the profile picture if required
            Text(
                text = "Contractor: ${contractor.email}",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Rating: ${contractor.overallRating}")
        }

        // Details about the contractor
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(text = "Experience: ${contractor.experience}")
            Text(text = "Speciality: ${contractor.speciality}")
            Text(text = "Contact: ${contractor.contact}")
            Text(text = "First Name: ${contractor.firstName}")
            Text(text = "Last Name: ${contractor.lastName}")
        }
    }
}
