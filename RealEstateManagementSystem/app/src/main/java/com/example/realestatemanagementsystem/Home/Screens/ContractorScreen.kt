package com.example.realestatemanagementsystem.Home.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.realestatemanagementsystem.contractor.Contractor
import com.example.realestatemanagementsystem.contractor.ContractorViewModel
import com.example.realestatemanagementsystem.review.ReviewViewModel
import com.example.realestatemanagementsystem.util.ContractorCard
import kotlinx.coroutines.launch

@Composable
fun ContractorScreen(modifier: Modifier = Modifier,contractorViewModel: ContractorViewModel,innerPadding:PaddingValues,navHostController: NavHostController,email:String,reviewViewModel: ReviewViewModel) {

    var showDialog by remember { mutableStateOf(false) }

    val allContractors by contractorViewModel.contractors.observeAsState(emptyList())
    var contractor by remember { mutableStateOf<Contractor?>(null) }
    val scope= rememberCoroutineScope()
    fun refreshContractor(){
        scope.launch {
        contractorViewModel.fetchAllContractorsWithDetails()
    }}
    LaunchedEffect(Unit) {
            contractorViewModel.fetchAllContractorsWithDetails()
            contractor=contractorViewModel.getContractorByEmail(email)

        }
    Column( modifier = Modifier
        .padding(innerPadding)
        ) {
        if (allContractors.isEmpty()) {
            // Show message if no contractors
            Box(contentAlignment = Alignment.Center,modifier = Modifier
                .weight(1f)
                .fillMaxWidth()){
           Text(text = "No contractors available")}
        } else {

                LazyColumn(modifier=Modifier.weight(1f)) {
                    items(allContractors) { contractor ->
                        // Display each property (replace with your card implementation)
                        ContractorCard(
                            contractor = contractor,
                            modifier = Modifier,
                            innerPadding = innerPadding,
                            reviewViewModel = reviewViewModel,
                            contractorViewModel = contractorViewModel,
                            onRefresh = {
                                contractorViewModel.fetchAllContractorsWithDetails()
                                Log.d("ContractorScreen", "Refreshed contractors")
                            }
                        )


                    }
                }


        }
        if (contractor == null) { // Show the button only if the user is not a contractor
            Button(
                onClick = {
                    showDialog = true
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.Red,
                    disabledContentColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Register")
            }
        }
    }
    if (showDialog) {
        Dialog (onDismissRequest = {showDialog=false})  {
          Box(modifier = Modifier

              .wrapContentHeight()
              .padding(16.dp)
              .background(
                  color = Color.White, // Set the background color
                  shape = RoundedCornerShape(12.dp)) ){  ContractorFormScreen(email = email, contractorViewModel = contractorViewModel, onRegistrationComplete = {
                showDialog=false
                navHostController.popBackStack()
            })}
        }

    }
}


